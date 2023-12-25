package ru.yandex.practicum.filmorate.error;

import ru.yandex.practicum.filmorate.utils.DefaultData;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super(DefaultData.ENTITY_NOT_FOUND_ERROR);
    }

}
