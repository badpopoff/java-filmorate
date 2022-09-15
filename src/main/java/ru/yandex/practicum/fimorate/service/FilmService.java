package ru.yandex.practicum.fimorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.fimorate.model.Film;
import ru.yandex.practicum.fimorate.storage.film.FilmStorage;
import ru.yandex.practicum.fimorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService implements FilmStorage {
    private final UserService userService;
    private InMemoryFilmStorage filmStorage;

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void setLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        userService.getById(userId);
        film.getLikedUsers().add(userId);
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        userService.getById(userId);
        film.getLikedUsers().remove(userId);
    }

    public List<Film> findPopularFilms(int size) {
        return filmStorage.findAll()
                .stream()
                .sorted((f0, f1) -> f1.getLikedUsers().size() - f0.getLikedUsers().size())
                .limit(size)
                .collect(Collectors.toList());
    }
}
