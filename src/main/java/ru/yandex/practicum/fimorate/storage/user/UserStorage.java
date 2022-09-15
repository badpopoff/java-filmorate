package ru.yandex.practicum.fimorate.storage.user;

import ru.yandex.practicum.fimorate.model.User;
import java.util.Collection;

public interface UserStorage {

    User create(User user);

    User updateUser(User user);

    User getById(int id);

    Collection<User> findAll();
}
