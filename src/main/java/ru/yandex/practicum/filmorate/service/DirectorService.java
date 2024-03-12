package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

public interface DirectorService {
    List<Catalog> getAll();

    Catalog getById(Long id);

    Catalog createDirector(Catalog catalog);

    Catalog updateDirector(Catalog catalog);

    void deleteById(Long id);
}
