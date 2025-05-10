package ru.romanov.voteservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.voteservice.model.User;
import ru.romanov.voteservice.model.dto.RegistrationForm;
import ru.romanov.voteservice.model.enums.Role;
import ru.romanov.voteservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public void register(RegistrationForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Логин занят");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        save(User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword().trim()))
                .email(form.getEmail())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .patronymic(form.getPatronymic())
                .phoneNumber(form.getPhoneNumber())
                .registrationDate(LocalDateTime.now())
                .roles(roles)
                .build());
    }

    public void addRole(String username, Role role) {
        setRole(username, role, true);
    }


    public void invokeRole(String username, Role role) {
        setRole(username, role, false);
    }

    private void setRole(String username, Role role, boolean isAdded) {
        if (!getCurrentUser().getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Access denied");
        } else {
            User user = findByUsername(username);
            if (!user.getRoles().contains(role)) {
                if (isAdded) user.getRoles().add(role);
                else user.getRoles().remove(role);

                save(user);
            }
        }
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsername(username);
    }

    // todo доработать нужно
    public void changePassword(String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 8) {
            throw new IllegalArgumentException("Новый пароль не удовлетворяет условиям");
        }

        var currentUser = getCurrentUser();
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("Введён неверный пароль");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword.trim()));
    }
}
