package ru.romanov.voteservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import ru.romanov.voteservice.model.User;
import ru.romanov.voteservice.service.UserService;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers("/voting/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/vote/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/dashboard").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Связь с вашим контроллером
                        .loginProcessingUrl("/login") // URL для POST-запроса
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
//                        .invalidateHttpSession(true)
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/**")
                )
        ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
//            log.info("user.getRole().name(): {}", AuthorityUtils.createAuthorityList(user.getRoles()));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(
                            user.getRoles().stream()
                                    .map(role -> "ROLE_" + role.name())
                                    .toList()));
        };
    }
}
