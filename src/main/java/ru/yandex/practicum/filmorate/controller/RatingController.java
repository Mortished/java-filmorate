package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.impl.RatingServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingController {

    private final RatingServiceImpl ratingServiceImpl;

    public RatingController(RatingServiceImpl ratingServiceImpl) {
        this.ratingServiceImpl = ratingServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return ratingServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return ratingServiceImpl.getById(id);
    }
}
