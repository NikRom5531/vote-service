package ru.romanov.voteservice.model;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
}
