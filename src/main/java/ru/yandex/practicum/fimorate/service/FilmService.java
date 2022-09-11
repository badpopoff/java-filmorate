package ru.yandex.practicum.fimorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.fimorate.model.Film;
import ru.yandex.practicum.fimorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends InMemoryFilmStorage{
    private final UserService userService;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public void setLike(int id, int userId) {
        Film film = super.getFilmById(id);
        userService.getById(userId);
        film.getIdLikeUsers().add(userId);
    }

    public void deleteLike(int id, int userId) {
        Film film = super.getFilmById(id);
        userService.getById(userId);
        film.getIdLikeUsers().remove(userId);
    }

    public List<Film> findPopularFilms(int size) {
        List<Film> films = super.findAll().stream().
                sorted((f0, f1) -> f1.getIdLikeUsers().size() - f0.getIdLikeUsers().size()).
                limit(size).collect(Collectors.toList());
        return films;
    }
}
