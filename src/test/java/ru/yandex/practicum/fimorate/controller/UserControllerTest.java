package ru.yandex.practicum.fimorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void shouldGetExceptionIfUserHasNotEmail() {
        User user = new User("", "Login", LocalDate.of(2006, 10, 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController().create(user));
        assertEquals("Адрес электронной почты не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotAtInEmail() {
        User user = new User("user1.usersandex.ru", "Login", LocalDate.of(2006, 10
                , 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController().create(user));
        assertEquals("Электронная почта должна содержать символ @.", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotLogin() {
        User user = new User("user1.users@yandex.ru", "", LocalDate.of(2006, 10
                , 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController().create(user));
        assertEquals("Логин не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfLoginContainsBlanks() {
        User user = new User("user1.users@yandex.ru", "Login 1", LocalDate.of(2006, 10
                , 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController().create(user));
        assertEquals("Логин не может содержать пробелы!", exception.getMessage());
    }

    @Test
    void shouldGetNameEqualLoginIfUserNameIsEmpty() {
        String login = "Login1";
        User user = new User("user1.users@yandex.ru", login, LocalDate.of(2006, 10, 10));
        user.setName("");
        UserController controller = new UserController();
        final String name = controller.create(user).getName();
        assertEquals(name, login);
    }

    @Test
    void shouldGetExceptionIfUserHasBirthdayAfterNow() {
        LocalDate birthday = LocalDate.now().plusDays(1);
        User user = new User("user1.users@yandex.ru", "Login1", birthday);
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController().create(user));
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfYouCreateUserAlreadyRegistered() {
        User user = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10
                , 10));
        user.setName("Name");
        UserController controller = new UserController();
        User checkUser = controller.create(user);

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> controller.create(checkUser));
        String checkText = "Пользователь с электронной почтой уже зарегистрирован!";
        assertEquals(checkText, exception.getMessage());
    }
}