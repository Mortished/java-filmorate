package ru.yandex.practicum.filmorate.error;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super("Данных с id = " + message + " не существует!");
    }
}
