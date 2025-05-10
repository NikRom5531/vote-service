package ru.romanov.voteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.romanov.voteservice.model.dto.LoginForm;
import ru.romanov.voteservice.model.dto.RegistrationForm;
import ru.romanov.voteservice.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                               BindingResult result) {
        if (result.hasErrors()) return "login";
        return "redirect:/login?success=registration";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegistrationForm form,
                               BindingResult result) {
        if (result.hasErrors()) return "register";

        userService.register(form);
        return "redirect:/login?success=registration";
    }
}
