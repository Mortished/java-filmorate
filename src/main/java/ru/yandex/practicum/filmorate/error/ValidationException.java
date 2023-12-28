package ru.yandex.practicum.filmorate.error;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
