package ru.yandex.practicum.fimorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String name;
    @Email
    private final String email;
    private final String login;
    private final LocalDate birthday;

}

