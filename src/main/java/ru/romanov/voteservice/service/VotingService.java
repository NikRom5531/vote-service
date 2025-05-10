package ru.romanov.voteservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.voteservice.model.Question;
import ru.romanov.voteservice.model.User;
import ru.romanov.voteservice.model.Vote;
import ru.romanov.voteservice.model.Voting;
import ru.romanov.voteservice.model.dto.AnswerDto;
import ru.romanov.voteservice.repository.VoteRepository;
import ru.romanov.voteservice.repository.VotingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingService {

    private final UserService userService;
    private final VotingRepository votingRepository;
    private final VoteRepository voteRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(Voting voting) {
        validateVoting(voting);
        voting.setCreatedBy(userService.getCurrentUser());
        voting.setCreatedAt(LocalDateTime.now());
        votingRepository.save(voting);
    }

    private void validateVoting(Voting voting) {
        if (voting.getQuestions() == null || voting.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Голосование должно содержать хотя бы один вопрос");
        }
        if (voting.isClosed() && (voting.getPassword() == null || voting.getPassword().isEmpty())) {
            throw new IllegalArgumentException("Для закрытого голосования требуется пароль");
        }
        if (voting.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Дата окончания должна быть в будущем");
        }
    }

    @Transactional
    public void recordVote(UUID votingId, List<AnswerDto> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Ответы отсутствуют");
        }

        LocalDateTime votedTime = LocalDateTime.now();
        Voting voting = findById(votingId);

        if (voting.getEndDate().isBefore(votedTime)) {
            throw new RuntimeException("Голосование завершено");
        }

        checkVotedUser(voting);

        try {
            answers.forEach(ans -> {
                log.info("questionId: {}", ans.getQuestionId().toString());
                Question question = voting.getQuestions()
                        .stream()
                        .filter(q -> q.getId().equals(ans.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Вопрос id=" + ans.getQuestionId() + " в голосовании id=" + voting.getId() + " не найден"));

                Vote vote = Vote.builder()
                        .voting(voting)
                        .question(question)
                        .user(voting.isAnonymous() ? null : userService.getCurrentUser())
                        .votedTime(votedTime)
                        .build();

                vote.setSelectedOptions(ans.getAnswer());

                voteRepository.save(vote);
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }

        addVotedUser(voting);
    }

    public Voting getVotingResults(UUID votingId, String password) {
        Voting voting = findById(votingId);

        if (voting.isClosed()) {
            if (!voting.getCreatedBy().equals(userService.getCurrentUser()) && !isVotedUser(voting)) {
                if (!password.equals(voting.getPassword())) throw new RuntimeException("Неверный пароль");
            }
        }

        return voting;
    }

    public List<Voting> getCurrentUserVotingList() {
        return List.of(); // todo удалить
//        return votingRepository.findByCreatedByOrVotedUsersContaining(userService.getCurrentUser());
    }

    public Voting findById(UUID votingId) {
        return votingRepository.findById(votingId)
                .orElseThrow(() -> new RuntimeException("Голосование не найдено"));
    }

    public List<Voting> findLastVotingByUser(User user) {
        return votingRepository.findByCreatedByOrderByCreatedAtDesc(user)
                .stream()
                .limit(10)
                .toList();
    }

    private void checkVotedUser(Voting voting) {
        if (isVotedUser(voting)) throw new RuntimeException("Вы уже участвовали в этом голосовании");
    }

    private boolean isVotedUser(Voting voting) {
        String userId = userService.getCurrentUser().getId().toString();
        return voting.getHashedUserIds().stream().anyMatch(hashUserId -> passwordEncoder.matches(userId, hashUserId));
    }

    private void addVotedUser(Voting voting) {
        voting.getHashedUserIds().add(passwordEncoder.encode(userService.getCurrentUser().getId().toString()));
    }
}
