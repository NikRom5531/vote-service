package ru.romanov.voteservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanov.voteservice.model.Question;
import ru.romanov.voteservice.model.Vote;
import ru.romanov.voteservice.model.VotingSession;
import ru.romanov.voteservice.model.enums.AnswerType;
import ru.romanov.voteservice.repository.QuestionRepository;
import ru.romanov.voteservice.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessVoteService {

    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;

    public Vote vote(UUID questionId, String voterName, List<String> selectedOptions, String password) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        VotingSession votingSession = question.getVotingSession();

        if (!votingSession.isOpen() && !votingSession.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        if (votingSession.getEndTime() != null && LocalDateTime.now().isAfter(votingSession.getEndTime())) {
            throw new RuntimeException("Voting is closed");
        }

        // Проверка типа ответа
        if (question.getAnswerType() == AnswerType.SINGLE_CHOICE && selectedOptions.size() > 1) {
            throw new RuntimeException("Only one option can be selected for this question");
        }

        Vote vote = new Vote();
        vote.setVoterName(voterName);
        vote.setSelectedAnswers(selectedOptions);
        vote.setQuestion(question);

        return voteRepository.save(vote);
    }

    public List<Vote> getVotesByQuestionId(UUID questionId) {
        return voteRepository.findByQuestionId(questionId);
    }

    public Map<UUID, Map<String, Long>> getResultsByVotingId(UUID votingId) {
        // Получаем все вопросы для данного голосования
        List<Question> questions = questionRepository.findByVotingId(votingId);

        // Создаем Map для хранения результатов
        Map<UUID, Map<String, Long>> results = new HashMap<>();

        // Для каждого вопроса считаем голоса
        for (Question question : questions) {
            // Получаем все голоса для текущего вопроса
            List<Vote> votes = voteRepository.findByQuestionId(question.getId());

            // Создаем Map для хранения результатов по вариантам ответа
            Map<String, Long> optionCounts = new HashMap<>();

            // Считаем голоса для каждого варианта ответа
            for (Vote vote : votes) {
                for (String selectedOption : vote.getSelectedAnswers()) {
                    optionCounts.put(selectedOption, optionCounts.getOrDefault(selectedOption, 0L) + 1);
                }
            }

            // Добавляем результаты для текущего вопроса в общую Map
            results.put(question.getId(), optionCounts);
        }

        return results;
    }
}
