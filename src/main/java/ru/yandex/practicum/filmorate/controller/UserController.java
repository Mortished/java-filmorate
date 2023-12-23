package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static ru.yandex.practicum.filmorate.utils.DefaultData.*;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User body) throws JsonProcessingException {
        try {
            validateUser(body);
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
        users.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User body) throws JsonProcessingException {
        if (!users.containsKey(id)) {
            log.warn(ENTITY_NOT_FOUND_ERROR);
            return ResponseEntity.notFound().build();
        }
        try {
            validateUser(body);
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
        users.put(id, body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private void validateUser(User body) throws ValidationException {
        if (body.getEmail() == null || !body.getEmail().matches(EMAIL_REGEX)) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (body.getLogin() == null || body.getLogin().isEmpty() || body.getLogin().chars().anyMatch(Character::isWhitespace)) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (body.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

}
