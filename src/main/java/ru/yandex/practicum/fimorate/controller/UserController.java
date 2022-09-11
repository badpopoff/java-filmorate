package ru.yandex.practicum.fimorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.User;
import ru.yandex.practicum.fimorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findFilmById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> findFriendsById(@PathVariable Integer id) {
        return userService.getAllFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validateUser(user);
        return userService.put(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            String message = "Адрес электронной почты не может быть пустым!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (!user.getEmail().contains("@")) {
            String message = "Электронная почта должна содержать символ @.";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            String message = "Логин не может быть пустым!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getLogin().contains(" ")) {
            String message = "Логин не может содержать пробелы!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Не обнаружено имя пользователя. Вместо имени будет использоваться логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем!";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}