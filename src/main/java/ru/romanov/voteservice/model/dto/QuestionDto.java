package ru.romanov.voteservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.romanov.voteservice.model.enums.QuestionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    @NotBlank(message = "Текст вопроса обязателен")
    private String text;

    private QuestionType type;

    private String options;

    private boolean required;
}
