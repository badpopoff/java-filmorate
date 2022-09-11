package ru.yandex.practicum.fimorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Film;
import ru.yandex.practicum.fimorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate FIRST_FILM_CREATION_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private final FilmService filmService;

    @GetMapping()
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film filmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(required = false, defaultValue = "10") String count) {
        return filmService.findPopularFilms(Integer.parseInt(count));
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping()
    public Film put(@Valid @RequestBody Film film) {
        validateFilm(film);
        return filmService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.setLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
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
