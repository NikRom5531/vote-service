package ru.romanov.voteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.voteservice.model.Question;
import ru.romanov.voteservice.model.Vote;
import ru.romanov.voteservice.model.VotingSession;
import ru.romanov.voteservice.service.ProcessVoteService;
import ru.romanov.voteservice.service.CreateVoteService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final ProcessVoteService processVoteService;
    private final CreateVoteService createVoteService;

    @PostMapping("/vote")
    public String vote(@RequestParam UUID votingId,
                       @RequestParam String voterName,
                       @RequestParam List<String> selectedOptions) {
        // Предположим, что первый вопрос в голосовании — это тот, на который голосуют
        VotingSession votingSession = createVoteService.getVotingById(votingId);
        Question question = votingSession.getQuestions().getFirst();

        processVoteService.vote(question.getId(), voterName, selectedOptions, votingSession.getPassword());
        return "redirect:/session/" + votingId; // Перенаправление на страницу голосования
    }

    @GetMapping("/{questionId}")
    public List<Vote> getVotes(@PathVariable UUID questionId) {
        return processVoteService.getVotesByQuestionId(questionId);
    }

    @GetMapping("/{questionId}/results")
    public Map<String, Long> getResults(@PathVariable UUID questionId) {
        List<Vote> votes = processVoteService.getVotesByQuestionId(questionId);

        return votes.stream()
                .flatMap(vote -> vote.getSelectedAnswers().stream())
                .collect(Collectors.groupingBy(option -> option, Collectors.counting()));
    }
}
