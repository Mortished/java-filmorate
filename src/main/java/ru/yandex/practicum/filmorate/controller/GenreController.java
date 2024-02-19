package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreServiceImpl genreServiceImpl;

    public GenreController(GenreServiceImpl genreServiceImpl) {
        this.genreServiceImpl = genreServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return genreServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return genreServiceImpl.getById(id);
    }


}
