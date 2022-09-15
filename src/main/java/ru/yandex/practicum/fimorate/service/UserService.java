package ru.yandex.practicum.fimorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.fimorate.model.User;
import ru.yandex.practicum.fimorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.fimorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserStorage {
    private final InMemoryUserStorage userStorage;

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public User getById(int id) {
        return userStorage.getById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public Set<User> getAllFriendsById(int id) {
        Set<Integer> idFriends = userStorage.getById(id).getFriends();
        if (idFriends.isEmpty()){
            return Collections.emptySet();
        }
        Set<User> friends = new HashSet<>();
        idFriends.forEach((i -> userStorage.findAll().forEach(user -> {if(user.getId() == i){friends.add(user);}})));
        return friends;
    }

    public Set<User> getCommonFriends(int id, int otherId){
        Set<Integer> idCommonFriends = new HashSet<>();
        idCommonFriends.addAll(userStorage.getById(id).getFriends());
        idCommonFriends.retainAll(userStorage.getById(otherId).getFriends());
        if(idCommonFriends.isEmpty()){
            return Collections.emptySet();
        }
        Set<User> friends = new HashSet<>();
        idCommonFriends.forEach(i -> userStorage.findAll().forEach(user -> {if(user.getId() == i){friends.add(user);}}));
        return friends;
    }
}
