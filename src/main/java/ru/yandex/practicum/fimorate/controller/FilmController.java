package ru.yandex.practicum.fimorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Film;
import ru.yandex.practicum.fimorate.model.Generator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate FIRST_FILM_CREATION_DATE = LocalDate.of(1895, 12, 28);
    private static  final int MAX_DESCRIPTION_LENGTH = 200;

    private final Map<Integer, Film> films = new HashMap<>();
    private final Generator generator = new Generator();

    @GetMapping()
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            String message = "Фильм с id " + film.getId() + " уже существует";
            log.error(message);
            throw new ValidationException(message);
        }
        validateFilm(film);
        int id = generator.getNewId();
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен фильм. id: {}, Имя: {}, Описание: {}, Длительность: {}, Год релиза: {}",
                id, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        return film;
    }

    @PutMapping()
    public Film put(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "Такого фильма нет в базе!";
            log.info(message);
            throw new ValidationException(message);
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Обновлён фильм. id: {}, Имя: {}, Описание: {}, Длительность: {}, Год релиза: {}",
                film.getId(), film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String message = "Не указано имя фильма!";
            log.error(message);
            throw new ValidationException("Не указано имя фильма!");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            String message = "Описание фильма слишком длинное. Допустимо не более 200 символов!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDuration() < 0) {
            String message = "Ошибка ввода! Продолжительность дожна быть больше нуля!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_CREATION_DATE)) {
            String message = "Ошибка ввода! Дата релиза должна быть позже 28 декабря 1895 года!";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
