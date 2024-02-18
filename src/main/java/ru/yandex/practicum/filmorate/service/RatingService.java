package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.storage.RatingDbStorage;

import java.util.List;

@Service
public class RatingService {

    private final RatingDbStorage ratingStorage;

    public RatingService(RatingDbStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Catalog> getAll() {
        return ratingStorage.getAll();
    }

    public Catalog getById(Long id) {
        return ratingStorage.getById(id);
    }
}
