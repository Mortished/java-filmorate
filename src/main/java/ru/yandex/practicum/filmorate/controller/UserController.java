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
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_NOT_FOUND_ERROR;
import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User body) {
        validateUser(body);

        users.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable Long id, @RequestBody User body) {
        if (!users.containsKey(id)) {
            log.warn(ENTITY_NOT_FOUND_ERROR);
            return ResponseEntity.notFound().build();
        }
        validateUser(body);

        users.put(id, body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private void validateUser(User body) throws ValidationException {
        if (body.getLogin().chars().anyMatch(Character::isWhitespace)) {
            ValidationException e = new ValidationException("Логин не может быть пустым и содержать пробелы!");
            log.warn(e.getMessage());
            throw e;
        }
        if (body.getBirthday().isAfter(LocalDate.now())) {
            ValidationException e = new ValidationException("Дата рождения не может быть в будущем!");
            log.warn(e.getMessage());
            throw e;
        }
    }

}
