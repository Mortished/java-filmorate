package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    void removeFilmById(Long id);

    Film getFilmById(Long id);

    List<Film> getPopularFilms(Long count, Long genreId, Integer year);

    List<Film> getRecommendations(Long userId);

    List<Film> search(String query, String by);

    List<Film> getFilmsByUser(Long userId);
}
