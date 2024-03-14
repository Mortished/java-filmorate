package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.impl.RatingDbStorageImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingDbStorageImpl ratingStorage;

    @Override
    public List<Catalog> getAll() {
        return ratingStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return ratingStorage.getById(id);
    }
}
