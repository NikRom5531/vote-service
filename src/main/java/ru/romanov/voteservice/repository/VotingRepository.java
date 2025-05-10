package ru.romanov.voteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.romanov.voteservice.model.User;
import ru.romanov.voteservice.model.Voting;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotingRepository extends JpaRepository<Voting, UUID> {

    @Query("SELECT v FROM Voting v WHERE v.createdBy = :user ORDER BY v.createdAt DESC")
    List<Voting> findByCreatedByOrderByCreatedAtDesc(@Param("user") User user);

//    List<Voting> findByCreatedByOrVotedUsersContaining(User currentUser);
}
