package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

public interface CatalogStorage {
    List<Catalog> getAll();

    Catalog getById(Long id);

}
