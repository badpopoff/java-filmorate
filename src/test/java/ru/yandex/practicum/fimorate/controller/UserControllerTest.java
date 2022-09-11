package ru.yandex.practicum.fimorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.User;
import ru.yandex.practicum.fimorate.service.UserService;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void shouldGetExceptionIfUserHasNotEmail() {
        User user = new User("", "Login", LocalDate.of(2006, 10, 10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController(new UserService()).create(user));
        assertEquals("Адрес электронной почты не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotAtInEmail() {
        User user = new User("user1.usersandex.ru", "Login", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> new UserController(new UserService()).create(user));
        assertEquals("Электронная почта должна содержать символ @.", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfUserHasNotLogin() {
        User user = new User("user1.users@yandex.ru", "", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController(new UserService()).create(user));
        assertEquals("Логин не может быть пустым!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfLoginContainsBlanks() {
        User user = new User("user1.users@yandex.ru", "Login 1", LocalDate.of(2006, 10,
                10));
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> new UserController(new UserService()).create(user));
        assertEquals("Логин не может содержать пробелы!", exception.getMessage());
    }

    @Test
    void shouldGetNameEqualLoginIfUserNameIsEmpty() {
        String login = "Login1";
        User user = new User("user1.users@yandex.ru", login, LocalDate.of(2006, 10, 10));
        user.setName("");
        UserController controller = new UserController(new UserService());
        final String name = controller.create(user).getName();
        assertEquals(name, login);
    }

    @Test
    void shouldGetExceptionIfUserHasBirthdayAfterNow() {
        LocalDate birthday = LocalDate.now().plusDays(1);
        User user = new User("user1.users@yandex.ru", "Login1", birthday);
        user.setName("Name");

        final ValidationException exception = assertThrows(ValidationException.class
                , () -> new UserController(new UserService()).create(user));
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfYouCreateUserAlreadyRegistered() {
        User user = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user.setName("Name");
        UserController controller = new UserController(new UserService());
        User checkUser = controller.create(user);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(checkUser));
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
        UserController controller = new UserController(new UserService());
        User checkUser = controller.create(user1);
        User checkFriend = controller.create(user2);
        int id = checkUser.getId();
        int idFriend = checkFriend.getId();
        controller.putFriend(id, idFriend);

        assertTrue(user1.getFriends().contains(idFriend));
    }

    @Test
    void shouldGetListOfFriendsByUserId(){
        User user1 = new User("user1.users@yandex.ru", "Login1", LocalDate.of(2006, 10,
                10));
        user1.setName("Name");

        User user2 = new User("friend.users@yandex.ru", "Login2", LocalDate.of(2010, 5,
                15));
        user2.setName("Friend");

        UserController controller = new UserController(new UserService());
        User checkUser = controller.create(user1);
        User checkFriend = controller.create(user2);
        int id = checkUser.getId();
        int idFriend = checkFriend.getId();
        controller.putFriend(id, idFriend);

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

        UserController controller = new UserController(new UserService());
        controller.create(user);
        controller.create(friend1);
        controller.create(friend2);
        int id = user.getId();
        int idFriend1 = friend1.getId();
        int idFriend2 = friend2.getId();

        controller.putFriend(id, idFriend1);
        controller.putFriend(id, idFriend2);

        assertEquals(controller.findFriendsById(id).size(), 2);
    }
}