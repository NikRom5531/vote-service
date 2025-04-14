package ru.romanov.voteservice.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @Email(message = "Некорректный email")
    private String email;

    @Size(min = 6, message = "Пароль должен быть не короче 6 символов")
    private String password;

    @Transient
    private String confirmPassword;
    // Getters/Setters + валидация совпадения паролей
}