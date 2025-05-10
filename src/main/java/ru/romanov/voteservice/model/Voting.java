package ru.romanov.voteservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Название обязательно")
    private String title;

    private boolean anonymous;
    private boolean closed;
    private String password; // Заполняется, если closed = true

    @FutureOrPresent(message = "Дата окончания не может быть в прошлом")
    private LocalDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "voting_id")
    private List<Question> questions = new ArrayList<>();

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name = "voting_voted_users",
//            joinColumns = @JoinColumn(name = "voting_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"),
//            uniqueConstraints = @UniqueConstraint(columnNames = {"voting_id", "user_id"})
//    )
//    private Set<User> votedUsers = new HashSet<>();
    @ElementCollection
    @CollectionTable(
            name = "voting_voted_users",
            joinColumns = @JoinColumn(name = "voting_id")
    )
    @Column(name = "hashed_user_id", nullable = false)
    private Set<String> hashedUserIds = new HashSet<>();

    public int getVotedCount() {
        return this.hashedUserIds.size();
    }
}
