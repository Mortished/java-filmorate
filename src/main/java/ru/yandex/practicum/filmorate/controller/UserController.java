package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import ru.yandex.practicum.filmorate.service.impl.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final EventServiceImpl eventService;

    public UserController(UserServiceImpl userServiceImpl, EventServiceImpl eventService) {
        this.userServiceImpl = userServiceImpl;
        this.eventService = eventService;
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userServiceImpl.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User body) {
        return userServiceImpl.create(body);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User body) {
        return userServiceImpl.update(body);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userServiceImpl.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userServiceImpl.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        return userServiceImpl.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userServiceImpl.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userServiceImpl.getUserById(id);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Long id) {

        return userServiceImpl.getRecommendations(id);
    }

    @DeleteMapping("/{userId}")
    public void removeUserById(@PathVariable Long userId) {
        userServiceImpl.remove(userId);
    }

    @GetMapping("/{userId}/feed")
    public List<Event> getUserFeed(@NotNull @PathVariable Long userId) {

        return eventService.getEventsByUserId(userId);
    }

}
