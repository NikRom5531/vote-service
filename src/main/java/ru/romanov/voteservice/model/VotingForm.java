package ru.romanov.voteservice.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class VotingForm {
    private String title;
    private boolean anonymous;
    private boolean closed;
    private String password;
    private LocalDateTime endDate;
    private final List<Question> questions = new ArrayList<>();
    // Getters/Setters
}