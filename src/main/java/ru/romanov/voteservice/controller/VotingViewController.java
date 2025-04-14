package ru.romanov.voteservice.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.romanov.voteservice.model.VotingSession;
import ru.romanov.voteservice.service.ProcessVoteService;
import ru.romanov.voteservice.service.CreateVoteService;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class VotingViewController {

    private final CreateVoteService createVoteService;
    private final ProcessVoteService processVoteService;

//    @GetMapping("/session/{id}")
//    public String getVotingPage(@PathVariable UUID id, Model model) {
//        Voting voting = createVoteService.getVotingById(id);
//        model.addAttribute("voting", voting);
//        return "voting"; // Имя шаблона (voting.html)
//    }

    @GetMapping("/session/{id}/results")
    public String getResultsPage(@PathVariable UUID id, Model model) {
        VotingSession votingSession = createVoteService.getVotingById(id);
        Map<UUID, Map<String, Long>> results = processVoteService.getResultsByVotingId(id);
        model.addAttribute("voting", votingSession);
        model.addAttribute("results", results);
        return "results";
    }

    @GetMapping("/session/{id}")
    public String getVoting(
            @PathVariable UUID id,
            HttpSession session,
            Model model
    ) {
        VotingSession votingSession = createVoteService.getVotingById(id);

        if (!votingSession.isOpen() && !isPasswordVerified(session)) {
            model.addAttribute("voting", votingSession);
            return "voting";
        }

        model.addAttribute("voting", createVoteService.getFullVotingData(id));
        return "voting";
    }

    @PostMapping("/session/verify-password")
    public String verifyPassword(
            @RequestParam UUID votingId,
            @RequestParam String password,
            HttpSession session) {

        VotingSession votingSession = createVoteService.getVotingById(votingId);

        if (votingSession.getPassword().equals(password)) {
            session.setAttribute("passwordVerified", true);
            return "redirect:/voting/" + votingId;
        }

        return "redirect:/voting/" + votingId + "?error";
    }

    private boolean isPasswordVerified(HttpSession session) {
        return session.getAttribute("passwordVerified") != null;
    }
}
