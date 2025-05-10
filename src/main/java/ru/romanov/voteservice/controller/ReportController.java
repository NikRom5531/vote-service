package ru.romanov.voteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.voteservice.model.Voting;
import ru.romanov.voteservice.service.VotingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final VotingService votingService;

    @GetMapping("/{id}")
    public ResponseEntity<Voting> getVotingResults(@PathVariable UUID id, @RequestParam(required = false) String password) {
        Voting voting = votingService.getVotingResults(id, password);
        return ResponseEntity.ok(voting);
    }

    @GetMapping("/user-votings")
    public ResponseEntity<List<Voting>> getUserVotings() {
        List<Voting> votings = votingService.getCurrentUserVotingList();
        return ResponseEntity.ok(votings);
    }
}
