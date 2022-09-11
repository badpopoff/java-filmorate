package ru.yandex.practicum.fimorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.fimorate.exeption.NotFoundException;
import ru.yandex.practicum.fimorate.exeption.ValidationException;
import ru.yandex.practicum.fimorate.model.ErrorResponse;
import ru.yandex.practicum.fimorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.fimorate.storage.user.InMemoryUserStorage;

@RestControllerAdvice(value = "ru.yandex.practicum.fimorate.controller",
        assignableTypes = {InMemoryFilmStorage.class, InMemoryUserStorage.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации!", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(final NotFoundException e) {
        return new ErrorResponse("Ошибка данных!", e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFilmNotFound(final RuntimeException e) {
        return new ErrorResponse("Ошибка данных!", e.getMessage());
    }
}
