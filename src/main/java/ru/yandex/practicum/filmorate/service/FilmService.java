package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmService {

    List<Film> getAll();

    Film create(@Valid Film film);

    Film update(@Valid Film film);

    void remove(Long id);

    void likeFilm(Long filmId, Long userId);

    void dislikeFilm(Long filmId, Long userId);

    List<Film> getPopularFilms(Long count);

    Film getFilmById(Long id);

    public List<Film> getDirectorFilms(long directorId, String sortBy);
}
