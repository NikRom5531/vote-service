package ru.romanov.voteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanov.voteservice.model.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByVotingId(UUID votingId);
}
