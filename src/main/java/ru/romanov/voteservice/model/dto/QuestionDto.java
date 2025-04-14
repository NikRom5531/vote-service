package ru.romanov.voteservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.romanov.voteservice.model.enums.AnswerType;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDto {

    private String text; // Текст вопроса

    private List<String> answerVariant; // Варианты ответов

    private AnswerType answerType; // Тип ответа
}
