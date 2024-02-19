package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.utils.FilmIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@Service
@Validated
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmDbStorageImpl filmStorage;
    private final UserDbStorageImpl userStorage;

    public FilmServiceImpl(FilmDbStorageImpl filmStorage, UserDbStorageImpl userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        filmStorage.save(film);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validateFilm(film);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, film);
        return filmStorage.update(film);
    }

    @Override
    public void likeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id);
        filmStorage.likeFilm(userId, id);
    }

    @Override
    public void dislikeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id);
        filmStorage.dislikeFilm(userId, id);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
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
}