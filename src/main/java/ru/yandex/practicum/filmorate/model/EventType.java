package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {

    LIKE(1),
    REVIEW(2),
    FRIEND(3);


    @Getter
    private final Integer id;
}
