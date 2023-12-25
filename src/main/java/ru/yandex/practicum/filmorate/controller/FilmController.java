package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidatingService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;

@RestController
@Slf4j
public class FilmController {

    private final ValidatingService validatingService;

    @Autowired
    public FilmController(ValidatingService validatingService) {
        this.validatingService = validatingService;
    }

    private final HashMap<Long, Film> library = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(library.values());
    }

    @PostMapping("/films")
    public ResponseEntity<Object> createFilm(@Valid @RequestBody Film body) {
        validatingService.validateFilm(body);

        library.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @PutMapping("/films")
    public ResponseEntity<Object> updateFilm(@Valid @RequestBody Film body) {
        if (!library.containsKey(body.getId())) {
            throw new NotFoundException();
        }
        validatingService.validateFilm(body);

        library.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

}
