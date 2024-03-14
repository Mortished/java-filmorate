package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.EventDbStorage;
import ru.yandex.practicum.filmorate.utils.FilmIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DirectorStorage directorStorage;
    private final EventDbStorage eventStorage;

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
    public void remove(Long id) {
        filmStorage.removeFilmById(id);
    }

    @Override
    public void likeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id);
        filmStorage.likeFilm(userId, id);
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.ADD, id));
    }

    @Override
    public void dislikeFilm(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.getFilmById(id);
        filmStorage.dislikeFilm(userId, id);
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.REMOVE, id));
    }

    @Override
    public List<Film> getPopularFilms(Long count, Long genreId, Integer year) {
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    @Override
    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    @Override
    public List<Film> getDirectorFilms(long directorId, String sortBy) {
        return directorStorage.getDirectorFilms(directorId, sortBy);
    }

    @Override
    public List<Film> search(String query, String by) {
        return filmStorage.search(query, by);
    }

    @Override
    public List<Film> getPopularFilmListOfUserAndFriend(Long userId, Long friendId) {
        List<Film> first = filmStorage.getFilmsByUser(userId);
        List<Film> second = filmStorage.getFilmsByUser(friendId);
        first.retainAll(second);
        return first;
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
