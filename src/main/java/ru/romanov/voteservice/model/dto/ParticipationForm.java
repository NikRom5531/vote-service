package ru.romanov.voteservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationForm {

    @NotNull(message = "Введите ID голосования")
    private UUID votingId;

    private String password; // Для закрытых голосований
}
