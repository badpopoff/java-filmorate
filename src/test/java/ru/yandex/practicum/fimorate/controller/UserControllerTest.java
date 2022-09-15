package ru.yandex.practicum.fimorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.User;
import ru.yandex.practicum.fimorate.service.UserService;
import ru.yandex.practicum.fimorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void shouldGetExceptionIfUserHasNotEmail() {
        User user = new User("", "Login", LocalDate.of(2006, 10, 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));

        assertEquals("Адрес электронной почты не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotAtInEmail() {
        User user = new User("user1.usersandex.ru", "Login", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));
        String message = "Электронная почта должна содержать символ @. Значение: " + user.getEmail();

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotLogin() {
        User user = new User("user1.users@yandex.ru", "", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));
        assertEquals("Логин не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfLoginContainsBlanks() {
        User user = new User("user1.users@yandex.ru", "Login 1", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));
        String message = "Логин не может содержать пробелы! Значение: " + user.getLogin();
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldGetNameEqualLoginIfUserNameIsEmpty() {
        String login = "Login1";
        User user = new User("user1.users@yandex.ru", login, LocalDate.of(2006, 10, 10));
        user.setName("");
        final String name = controller.create(user).getName();
        assertEquals(name, login);
    }

    @Test
    void shouldGetExceptionIfUserHasBirthdayAfterNow() {
        LocalDate birthday = LocalDate.now().plusDays(1);
        User user = new User("user1.users@yandex.ru", "Login1", birthday);
        user.setName("Name");
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));
        String message = "Дата рождения не может быть в будущем! Значение: " + user.getBirthday();
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfYouCreateUserAlreadyRegistered() {
        User user = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user.setName("Name");
        User checkUser = controller.create(user);

        final ValidationException exception = assertThrows(ValidationException.class,() ->
                controller.create(checkUser));
        String checkText = String.format(
                "Пользователь с электронной почтой %s уже зарегистрирован!",
                user.getEmail());
        assertEquals(checkText, exception.getMessage());
    }

    @Test
    void shouldAddFriend(){
        User user1 = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user1.setName("Name");

        User user2 = new User("friend.users@yandex.ru", "Login2", LocalDate.of(2010, 5,
                15));
        user2.setName("Friend");
        User checkUser = controller.create(user1);
        User checkFriend = controller.create(user2);
        int id = checkUser.getId();
        int friendId = checkFriend.getId();
        controller.putFriend(id, friendId);

        assertTrue(user1.getFriends().contains(friendId));
    }

    @Test
    void shouldGetListOfFriendsByUserId(){
        User user1 = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user1.setName("Name");

        User user2 = new User("friend.users@yandex.ru", "Login2", LocalDate.of(2010, 5,
                15));
        user2.setName("Friend");

        User checkUser = controller.create(user1);
        User checkFriend = controller.create(user2);
        int id = checkUser.getId();
        int friendId = checkFriend.getId();
        controller.putFriend(id, friendId);
        Set<User> friends = controller.findFriendsById(id);

        assertEquals(friends.size(), 1);
        assertTrue(friends.contains(checkFriend));
    }

    @Test
    void shouldGetAllFriend(){
        User user = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user.setName("Name");
        User friend1 = new User("friend1.users@yandex.ru", "Login2", LocalDate.of(2010, 5,
                15));
        friend1.setName("Friend1");
        User friend2 = new User("friend2.users@yandex.ru", "Login2", LocalDate.of(2010, 5,
                15));
        friend2.setName("Friend1");

        controller.create(user);
        controller.create(friend1);
        controller.create(friend2);
        int id = user.getId();
        int friend1Id = friend1.getId();
        int friend2Id = friend2.getId();

        controller.putFriend(id, friend1Id);
        controller.putFriend(id, friend2Id);

        assertEquals(controller.findFriendsById(id).size(), 2);
    }
}