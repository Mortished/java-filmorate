package ru.yandex.practicum.filmorate.utils;

import java.time.LocalDate;

public final class DefaultData {
    private DefaultData() {
        throw new UnsupportedOperationException();
    }
    public static final LocalDate FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    public static final String ENTITY_NOT_FOUND_ERROR = "Ошибка: Сущность не найдена! ";
    public static final String ENTITY_PROCESSED_SUCCESSFUL = "Сущность успешно добавлена/обновлена. {}";
}
