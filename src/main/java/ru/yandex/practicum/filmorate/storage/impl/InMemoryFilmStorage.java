package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> filmStorage = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Film save(Film film) {
        return filmStorage.put(film.getId(), film);
    }

    @Override
    public Film update(Film film) {
        if (!filmStorage.containsKey(film.getId())) {
            throw new NotFoundException();
        }
        return filmStorage.put(film.getId(), film);
    }

    @Override
    public void removeFilmById(Long id) {
    }

    @Override
    public Film getFilmById(Long id) {
        Film result = filmStorage.get(id);
        if (result == null) {
            throw new NotExistException(id.toString());
        }
        return result;
    }

    public List<Film> getPopularFilms(Long count, Long genreId, Integer year) {
        return Collections.emptyList();
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        return null;
    }
}
