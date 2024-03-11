package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

public interface GenreStorage {

    List<Catalog> getAll();

    Catalog getById(Long id);

    List<Catalog> getGenres(Long filmID);
}
