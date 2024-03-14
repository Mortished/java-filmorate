package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventOperation {

    ADD(1),
    UPDATE(2),
    REMOVE(3);

    @Getter
    private final Integer id;

}
