package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.storage.RatingDbStorageImpl;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingDbStorageImpl ratingStorage;

    public RatingServiceImpl(RatingDbStorageImpl ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    @Override
    public List<Catalog> getAll() {
        return ratingStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return ratingStorage.getById(id);
    }
}
