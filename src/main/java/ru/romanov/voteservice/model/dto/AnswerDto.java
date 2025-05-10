package ru.romanov.voteservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private UUID questionId;
    private List<String> answer = new ArrayList<>();
}
