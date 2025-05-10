package ru.romanov.voteservice.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotingForm {

    @NotBlank(message = "Название обязательно")
    private String title;

    private boolean anonymous;

    private boolean closed;

    private String password;

    @FutureOrPresent(message = "Дата окончания не может быть в прошлом")
    private LocalDateTime endDate;

    private List<QuestionDto> questions = new ArrayList<>();
}
