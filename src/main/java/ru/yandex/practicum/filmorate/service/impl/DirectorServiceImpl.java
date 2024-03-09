package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorStorage directorStorage;

    @Override
    public List<Catalog> getAll() {
        return directorStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return directorStorage.getById(id);
    }

    @Override
    public Catalog createDirector(Catalog catalog) {
        return directorStorage.save(catalog);
    }

    @Override
    public Catalog updateDirector(Catalog catalog) {
        return directorStorage.update(catalog);
    }

    @Override
    public void deleteById(Long id) {
        directorStorage.deleteById(id);
    }
}
