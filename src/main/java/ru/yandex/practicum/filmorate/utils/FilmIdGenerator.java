package ru.yandex.practicum.filmorate.utils;

public class FilmIdGenerator {

    private static FilmIdGenerator instance;
    private Long id = 1L;

    private FilmIdGenerator() {
    }

    public static FilmIdGenerator getInstance() {
        if (instance == null) {
            instance = new FilmIdGenerator();
        }
        return instance;
    }

    public Long getId() {
        long result = id;
        id++;
        return result;
    }
}