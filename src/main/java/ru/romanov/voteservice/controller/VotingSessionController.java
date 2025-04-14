package ru.romanov.voteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.voteservice.model.VotingSession;
import ru.romanov.voteservice.model.dto.VotingDto;
import ru.romanov.voteservice.service.CreateVoteService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class VotingSessionController {

    private final CreateVoteService createVoteService;

    @Value("${server-custom.url-address}")
    private String url;

    @PostMapping(value = "/create")
    public Map<String, String> create(@RequestBody VotingDto request) {
        VotingSession votingSession = createVoteService.create(request);

        Map<String, String> response = new HashMap<>();
        response.put("link", url + "/session/" + votingSession.getId());
        if (!votingSession.isOpen()) {
            response.put("password", votingSession.getPassword());
        }

        return response;
    }

//    @GetMapping("/{id}")
//    public Voting getVoting(@PathVariable Long id) {
//        return votingService.getVotingById(id);
//    }
}
