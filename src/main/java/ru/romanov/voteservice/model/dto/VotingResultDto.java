package ru.romanov.voteservice.model.dto;

import lombok.Data;
import ru.romanov.voteservice.model.Question;
import ru.romanov.voteservice.model.Voting;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VotingResultDto {
    private String title;
    private boolean anonymous;
    private LocalDateTime endDate;
    private int votedUserCount;
    private List<String> votedUsers;
    private Map<String, Integer> results;

//    public static VotingResultDto fromEntity(Voting voting) {
//        VotingResultDto dto = new VotingResultDto();
//        dto.setTitle(voting.getTitle());
//        dto.setAnonymous(voting.isAnonymous());
//        dto.setEndDate(voting.getEndDate());
//        dto.setVotedUserCount(voting.getHashedUserIds().size());
//
//        if (!voting.isAnonymous()) {
//            dto.setVotedUsers(voting.getHashedUserIds().stream()
//                    .map(user -> user.getLastName() + " " + user.getFirstName().charAt(0) + ".")
//                    .toList());
//        }
//
//        Map<String, Integer> results = new HashMap<>();
//        for (Question question : voting.getQuestions()) {
//            for (Answer answer : question.getAnswers()) {
//                results.put(answer.getText(), answer.getVotes());
//            }
//        }
//        dto.setResults(results);
//
//        return dto;
//    }
}
