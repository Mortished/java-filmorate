package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.utils.FilmIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@Service
@Validated
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(@Valid Film film) {
        validateFilm(film);
        filmStorage.save(film);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, film);
        return film;
    }

    public Film update(@Valid Film film) {
        validateFilm(film);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, film);
        filmStorage.update(film);
        return film;
    }


    private void validateFilm(@Valid Film film) {
        if (film.getReleaseDate().isBefore(FILM_RELEASE_DATE)) {
            ValidationException e = new ValidationException("Дата релиза (releaseDate) не может быть раньше 28.12.1895");
            log.warn(e.getMessage());
            throw e;
        }
        if (film.getId() == null) {
            film.setId(FilmIdGenerator.getInstance().getId());
        }
    }

    public void likeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getUserLikes().add(userId);
    }

    public void dislikeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getUserLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }
}
