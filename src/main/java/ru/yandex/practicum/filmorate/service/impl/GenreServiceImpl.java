package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorageImpl;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDbStorageImpl genreStorage;

    public GenreServiceImpl(GenreDbStorageImpl genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Catalog> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return genreStorage.getById(id);
    }
}
