package ru.yandex.practicum.fimorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.fimorate.exeption.NotFoundException;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Generator;
import ru.yandex.practicum.fimorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Generator generator = new Generator();

    @Override
    public User create(User user) {
        boolean checkedUser = users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (checkedUser) {
            String message = String.format(
                    "Пользователь с электронной почтой %s уже зарегистрирован!",
                    user.getEmail());
            log.error(message);
            throw new ValidationException(message);
        }
        int id = generator.getNewId();
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен полльзователь. id: {}, Имя: {}, E-mail {}, Логин: {}, День рождения: {}",
                id, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        return user;
    }

    @Override
    public User put(User user) {
        if (!users.containsKey(user.getId())) {
            String message = String.format(
                    "Пользователь с электронной почтой %s не зарегистрирован!",
                    user.getEmail());
            log.error(message);
            throw new NotFoundException(message);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById (int id){
       if (!users.containsKey(id)){
           String message = String.format(
                   "Пользователь с id %d не зарегистрирован!",
                   id);
           log.error(message);
           throw new NotFoundException(message);
       }
       return users.get(id);
    }

    @Override
    public Collection<User> findAll(){
        return users.values();
    }
}
