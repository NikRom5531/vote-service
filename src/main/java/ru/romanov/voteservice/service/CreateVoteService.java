package ru.romanov.voteservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.romanov.voteservice.model.Question;
import ru.romanov.voteservice.model.VotingSession;
import ru.romanov.voteservice.model.dto.QuestionDto;
import ru.romanov.voteservice.model.dto.VotingDto;
import ru.romanov.voteservice.repository.QuestionRepository;
import ru.romanov.voteservice.repository.VotingRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateVoteService {

    private final VotingRepository votingRepository;
    private final QuestionRepository questionRepository;

    public VotingSession create(VotingDto request) {
        if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be null or empty");
        }

        VotingSession votingSession = createVoting(request);
        List<Question> questions = request.getQuestions().stream()
                .map(q -> createQuestion(q, votingSession))
                .toList();

        return votingSession;
    }

    public VotingSession getVotingById(UUID id) {
        return votingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voting not found"));
    }

    public VotingSession getFullVotingData(UUID id) {
        VotingSession votingSession = getVotingById(id);

        if (!votingSession.isOpen()) {
            // Дополнительные проверки безопасности
//            SecurityContextHolder.getContext().checkPermissions();
        }

        votingSession.setQuestions(questionRepository.findByVotingId(id));

        return votingSession;
    }

    private VotingSession createVoting(VotingDto request) {
        return votingRepository.save(
                VotingSession.builder()
                        .title(request.getTitle())
                        .isAnonymous(request.isAnonymous())
                        .isOpen(request.isOpen())
                        .password(!request.isOpen() ? generatePassword() : null)
                        .endTime(request.getEndTime())
                        .maxVotes(request.getMaxVotes())
                        .build()
        );
    }

    private Question createQuestion(QuestionDto dto, VotingSession votingSession) {
        return questionRepository.save(
                Question.builder()
                        .text(dto.getText())
                        .answerType(dto.getAnswerType())
                        .answerVariant(dto.getAnswerVariant())
                        .votingSession(votingSession)
                        .build()
        );
    }

    private String generatePassword() {
        String s = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
        return s.substring(s.length() - 13, s.length() - 1);
    }
}
