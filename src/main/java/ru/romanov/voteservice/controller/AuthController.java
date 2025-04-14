package ru.romanov.voteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.romanov.voteservice.model.LoginForm;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginForm loginForm) {
        // Логика аутентификации (Spring Security или кастомная)
        return "redirect:/dashboard";
    }
}
