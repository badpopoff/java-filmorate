package ru.yandex.practicum.fimorate.exeption;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}
