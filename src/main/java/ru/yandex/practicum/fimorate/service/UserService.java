package ru.yandex.practicum.fimorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.fimorate.model.User;
import ru.yandex.practicum.fimorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService extends InMemoryUserStorage {

    public void addFriend(int id, int friendId) {
        User user = super.getById(id);
        User friend = super.getById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        User user = super.getById(id);
        User friend = super.getById(friendId);
        super.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public Set<User> getAllFriendsById(int id) {
        Set<Integer> idFriends = super.getById(id).getFriends();
        if (idFriends.isEmpty()){
            return Collections.emptySet();
        }
        Set<User> friends = new HashSet<>();
        idFriends.forEach((i -> super.findAll().forEach(user -> {if(user.getId() == i){friends.add(user);}})));
        return friends;
    }

    public Set<User> getCommonFriends(int id, int otherId){
        Set<Integer> idCommonFriends = new HashSet<>();
        idCommonFriends.addAll(super.getById(id).getFriends());
        idCommonFriends.retainAll(super.getById(otherId).getFriends());
        if(idCommonFriends.isEmpty()){
            return Collections.emptySet();
        }
        Set<User> friends = new HashSet<>();
        idCommonFriends.forEach(i -> super.findAll().forEach(user -> {if(user.getId() == i){friends.add(user);}}));
        return friends;
    }
}
