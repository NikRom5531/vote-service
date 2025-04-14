package ru.romanov.voteservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title; // Название голосования
    private boolean isAnonymous; // Анонимное или публичное
    private boolean isOpen; // Открытое или закрытое
    private String password; // Пароль (если закрытое)
    private LocalDateTime endTime; // Дата и время окончания
    private Integer maxVotes; // Максимальное количество голосов

    @OneToMany(mappedBy = "voting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;
}
