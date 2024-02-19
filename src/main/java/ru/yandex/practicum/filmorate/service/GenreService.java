package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

public interface GenreService {
    List<Catalog> getAll();
    Catalog getById(Long id);
}
