package ru.romanov.voteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanov.voteservice.model.VotingSession;

import java.util.UUID;

public interface VotingRepository extends JpaRepository<VotingSession, UUID> {
}
