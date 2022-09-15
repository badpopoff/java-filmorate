package ru.yandex.practicum.fimorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.fimorate.exeption.NotFoundException;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Film;
import ru.yandex.practicum.fimorate.model.Generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Generator generator = new Generator();

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            String message = "Фильм с id " + film.getId() + " уже существует";
            log.error(message);
            throw new ValidationException(message);
        }
        int id = generator.getNewId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "Фильм с id = " + film.getId() + " не обнаружен в базе!";
            log.info(message);
            throw new NotFoundException(message);
        }
        films.put(film.getId(), film);
        log.info("Обновлён фильм. id: {}, Имя: {}, Описание: {}, Длительность: {}, Год релиза: {}",
                film.getId(), film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            String message = "Фильм с id = " + id + " не обнаружен в базе!";
            log.info(message);
            throw new NotFoundException(message);
        }
        return films.get(id);
    }
}
