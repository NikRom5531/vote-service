package ru.romanov.voteservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
//    @Column(name = "voting_id", nullable = false)
    private Voting voting;

    @ManyToOne
//    @Column(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
//    @Column(name = "user_id")
    private User user;

    @ElementCollection
    private List<String> selectedOptions;

    @Column(nullable = false)
    private LocalDateTime votedTime;
}
