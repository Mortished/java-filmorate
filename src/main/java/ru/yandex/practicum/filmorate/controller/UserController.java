package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidatingService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;

@RestController
@Slf4j
public class UserController {
    private final ValidatingService validatingService;
    private final HashMap<Long, User> users = new HashMap<>();

    @Autowired
    public UserController(ValidatingService validatingService) {
        this.validatingService = validatingService;
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User body) {
        validatingService.validateUser(body);

        users.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return users.get(body.getId());
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User body) {
        if (!users.containsKey(body.getId())) {
            throw new NotFoundException();
        }
        validatingService.validateUser(body);

        users.put(body.getId(), body);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, body);
        return users.get(body.getId());
    }

}
