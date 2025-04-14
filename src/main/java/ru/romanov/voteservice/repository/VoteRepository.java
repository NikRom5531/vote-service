package ru.romanov.voteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanov.voteservice.model.Vote;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    List<Vote> findByQuestionId(UUID questionId);
}
