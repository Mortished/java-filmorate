package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> createFilm(@RequestBody Film body) {
        try {
            validateFilm(body);
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
        library.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @PutMapping("/films/{id}")
    public ResponseEntity<Object> updateFilm(@PathVariable Long id, @RequestBody Film body) {
        if (!library.containsKey(id)) {
            log.warn(ENTITY_NOT_FOUND_ERROR);
            return ResponseEntity.notFound().build();
        }
        try {
            validateFilm(body);
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
        library.put(id, body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private void validateFilm(Film body) throws ValidationException {
        if (body.getName() == null || body.getName().isEmpty()) {
            throw new ValidationException("Имя не должно быть пустым!");
        }
        if (body.getDescription().length() > 200) {
            throw new ValidationException("Длинна описания не может превышать 200 символов!");
        }
        if (body.getReleaseDate().isBefore(FILM_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (body.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

}
