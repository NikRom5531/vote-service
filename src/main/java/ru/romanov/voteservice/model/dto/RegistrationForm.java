package ru.romanov.voteservice.model.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm extends LoginForm {

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    private String patronymic; // Отчество (необязательное)

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;

    @AssertTrue(message = "Пароли не совпадают")
    private boolean isPasswordsMatch() {
        return getPassword() != null && getPassword().equals(confirmPassword);
    }
}
