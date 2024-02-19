package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

public interface RatingService {
    List<Catalog> getAll();
    Catalog getById(Long id);
}
