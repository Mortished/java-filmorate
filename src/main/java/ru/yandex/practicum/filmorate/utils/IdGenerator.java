package ru.yandex.practicum.filmorate.utils;

public class IdGenerator {

    private static IdGenerator instance;
    private Long id = 1L;

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public Long getId() {
        long result = id;
        id++;
        return result;
    }
}