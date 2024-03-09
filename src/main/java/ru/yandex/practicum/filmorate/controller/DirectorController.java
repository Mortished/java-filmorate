package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.impl.DirectorServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/directors")
@Slf4j
public class DirectorController {
    private final DirectorServiceImpl directorServiceImpl;

    public DirectorController(DirectorServiceImpl directorServiceImpl) {
        this.directorServiceImpl = directorServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return directorServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return directorServiceImpl.getById(id);
    }

    @PostMapping
    public Catalog createDirector(@Valid @RequestBody Catalog body) {
        return directorServiceImpl.createDirector(body);
    }

    @PutMapping
    public Catalog updateDirector(@Valid @RequestBody Catalog body) {
        return directorServiceImpl.updateDirector(body);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        directorServiceImpl.deleteById(id);
    }
}
