package ru.romanov.voteservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class VotingDto {

    private String title; // Название голосования

    private boolean isAnonymous; // Анонимное или публичное

    private boolean isOpen; // Открытое или закрытое

    private LocalDateTime endTime; // Дата и время окончания

    private Integer maxVotes; // Максимальное количество голосов

    private List<QuestionDto> questions;
}
