package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    private final FilmServiceImpl filmServiceImpl;

    public FilmController(FilmServiceImpl filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmServiceImpl.getAll();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film body) {
        return filmServiceImpl.create(body);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film body) {
        return filmServiceImpl.update(body);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmServiceImpl.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmServiceImpl.dislikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmList(@RequestParam(defaultValue = "10", required = false) Long count,
                                         @RequestParam(required = false) Long genreId,
                                         @RequestParam(required = false) Integer year) {
        return filmServiceImpl.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmServiceImpl.getFilmById(id);
    }

    @DeleteMapping("/{filmId}")
    public void removeFilmById(@PathVariable Long filmId) {
        filmServiceImpl.remove(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilms(@PathVariable(name = "directorId") Long directorId,
                                       @RequestParam String sortBy) {
        return filmServiceImpl.getDirectorFilms(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> findFilms(@NotEmpty @RequestParam String query,
                                @NotEmpty @RequestParam String by) {
        return filmServiceImpl.search(query, by);
    }

    @GetMapping("/common")
    public List<Film> getPopularFilmListOfUserAndFriend(@RequestParam(name = "userId") Long userId, @RequestParam(name = "friendId") Long friendId) {
        return filmServiceImpl.getPopularFilmListOfUserAndFriend(userId, friendId);
    }
}
