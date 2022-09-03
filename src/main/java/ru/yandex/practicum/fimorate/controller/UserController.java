package ru.yandex.practicum.fimorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Generator;
import ru.yandex.practicum.fimorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final Generator generator = new Generator();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с электронной почтой уже зарегистрирован!");
        }
        validateUser(user);
        int id = generator.getNewId();
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен полльзователь. id: {}, Имя: {}, E-mail {}, Логин: {}, День рождения: {}",
                id, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не зарегистрирован!");
        }
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            String message = "Адрес электронной почты не может быть пустым!";
            log.info(message);
            throw new ValidationException(message);
        }
        if (!user.getEmail().contains("@")) {
            String message = "Электронная почта должна содержать символ @.";
            log.info(message);
            throw new ValidationException(message);
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            String message = "Логин не может быть пустым!";
            log.info(message);
            throw new ValidationException(message);
        }
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы!";
            log.info(message);
            throw new ValidationException(message);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Не обнаружено имя пользователя. Вместо имени будет использоваться логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем!";
            log.info(message);
            throw new ValidationException(message);
        }
    }
}