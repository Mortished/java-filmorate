package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.*;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Long, Film> library = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(library.values());
    }

    @PostMapping("/films")
    public ResponseEntity<Object> createFilm(@Valid @RequestBody Film body) {
        validateFilm(body);

        library.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @PutMapping("/films/{id}")
    public ResponseEntity<Object> updateFilm(@Valid @PathVariable Long id, @RequestBody Film body) {
        if (!library.containsKey(id)) {
            log.warn(ENTITY_NOT_FOUND_ERROR);
            return ResponseEntity.notFound().build();
        }

        validateFilm(body);

        library.put(id, body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private void validateFilm(Film body) throws ValidationException {
        if (body.getDescription().length() > 200) {
            ValidationException e = new ValidationException("Длинна описания не может превышать 200 символов!");
            log.warn(e.getMessage());
            throw e;
        }
        if (body.getReleaseDate().isBefore(FILM_RELEASE_DATE)) {
            ValidationException e = new ValidationException("Дата релиза не может быть раньше 28.12.1895");
            log.warn(e.getMessage());
            throw e;
        }
        if (body.getDuration() < 1) {
            ValidationException e = new ValidationException("Продолжительность фильма должна быть положительной");
            log.warn(e.getMessage());
            throw e;
        }
    }

}
