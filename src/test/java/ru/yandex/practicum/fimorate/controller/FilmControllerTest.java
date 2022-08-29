package ru.yandex.practicum.fimorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    void shouldGetExceptionIfFilmHasNotName() {
        Film film = new Film(120, " ", LocalDate.of(2020, 2, 12),
                "Description");
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .createFilm(film));
        assertEquals("Не указано имя фильма!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfFilmHasNullInsteadName() {
        Film film = new Film( 120, null, LocalDate.of(2020, 2, 12),
                "Description");
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .createFilm(film));
        assertEquals("Не указано имя фильма!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfFilmHasDescriptionHasMore200Characters() {
        String description = "Паттерны проектирования. Прежде чем перейти к первой аннотации, разберём важный для её " +
                "понимания термин. На каком бы языке ни писал программист, в его работе постоянно возникают похожие" +
                " проблемы, для которых уже существуют образцы решения. Такие образцы называют паттернами " +
                "проектирования. Паттерны используются при разработке архитектуры программы. Они помогают " +
                "программисту не изобретать велосипед, а применять уже выработанные, проверенные концепции для " +
                "реализации поставленной задачи. Также паттерны стандартизируют процесс разработки. С помощью них " +
                "можно одним словом объяснить другому программисту, что делает тот или иной код. При этом паттерн " +
                "нельзя просто скопировать в свой проект. Его необходимо подстроить под нужды конкретной программы. " +
                "Существует 22 классических паттерна проектирования, каждый из которых подсказывает " +
                "оптимальное решение определённой проблемы. Паттерны делятся на три группы: порождающие применяются " +
                "для гибкого создания объектов без внесения в код лишних зависимостей; структурные показывают " +
                "различные способы построения связей между объектами; поведенческие описывают эффективную " +
                "коммуникацию между объектами. Не стоит путать паттерн с алгоритмом. Алгоритм — чёткая " +
                "последовательность действий для получения конкретного результата. Паттерн — высокоуровневое " +
                "описание решения задачи, которое может отличаться в двух разных программах. Алгоритм можно " +
                "сравнить с рецептом, в котором по пунктам расписано, как приготовить борщ с пампушками. " +
                "Паттерн — общее описание того, как сварить вкусный суп: необходимо  выбрать ингредиенты, " +
                "подготовить бульон, добавить приправы. Будут ли это щи или лагман — зависит от поставленной задачи";

        Film file = new Film(120, "Film name",
                LocalDate.of(2020, 2, 12), description);
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .createFilm(file));
        assertEquals("Описание фильма слишком длинное. Допустимо не более 200 символов!",
                exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfFilmHasReleaseDateBeforeFirstFilm() {
        LocalDate dateBeforeFirstFilm = LocalDate.of(1895, 12, 27);
        Film film = new Film(120, "Film name", dateBeforeFirstFilm, "Description");
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .createFilm(film));
        assertEquals("Ошибка ввода! Дата релиза должна быть позже 28 декабря 1895 года!"
                , exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfFilmHasDurationLess0() {
        long duration = -1;
        Film film = new Film( duration, "Film name", LocalDate.of(2020, 2, 12),
                "Description");
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .createFilm(film));
        assertEquals("Ошибка ввода! Продолжительность дожна быть больше нуля!", exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfYouCreateNewFilmExistsAlreadyInBase() {
        Film film = new Film(120, "Film name", LocalDate.of(2020, 2, 12),
                "Description");
        FilmController controller = new FilmController();
        Film checkFilm = controller.createFilm(film);
        final ValidationException exception = assertThrows(ValidationException.class, () -> controller
                .createFilm(checkFilm));
        String checkMessage = "Фильм с id " + film.getId() + " уже существует";
        assertEquals(checkMessage, exception.getMessage());
    }

    @Test
    void shouldGetExceptionIfYouPutFilmDoesNotExistInBase() {
        Film film = new Film(120, "Film name", LocalDate.of(2020, 2, 12),
                "Description");
        film.setId(1);
        final ValidationException exception = assertThrows(ValidationException.class, () -> new FilmController()
                .put(film));
        assertEquals("Такого фильма нет в базе!", exception.getMessage());
    }
}