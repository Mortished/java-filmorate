package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {
    List<Catalog> getAll();

    Catalog getById(Long id);

    Catalog save(Catalog catalog);

    Catalog update(Catalog catalog);

    void deleteById(Long id);
    public void addFilmDirectors(Long filmId, Set<Catalog> directors);
    public void updateFilmDirectors(Long filmId, Set<Catalog> directors);

    public List<Film> getDirectorFilms(Long directorId, String sortBy);
    public List<Catalog> getFilmDirectors(Long filmId);

}
