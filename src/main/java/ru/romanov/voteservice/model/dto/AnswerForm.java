package ru.romanov.voteservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerForm {

    private List<AnswerDto> answers = new ArrayList<>();
}
