package ru.yandex.practicum.fimorate.storage.film;

import ru.yandex.practicum.fimorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> findAll();

    Film getFilmById(int id);
}
