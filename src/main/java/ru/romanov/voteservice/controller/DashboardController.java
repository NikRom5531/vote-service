package ru.romanov.voteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.romanov.voteservice.model.User;
import ru.romanov.voteservice.service.UserService;
import ru.romanov.voteservice.service.VotingService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final VotingService votingService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);

        model.addAttribute("recentVotings", votingService.findLastVotingByUser(user));

        return "dashboard";
    }
}
