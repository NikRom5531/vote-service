package ru.romanov.voteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.romanov.voteservice.model.Vote;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.voting.id = :votingId")
    long countByVotingId(@Param("votingId") UUID votingId);
}