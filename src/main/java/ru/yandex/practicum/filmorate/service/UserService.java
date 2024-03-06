package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    List<User> getAll();

    User create(@Valid User user);

    User update(@Valid User user);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    User getUserById(Long id);

    List<Film> getRecommendations(Long userId);
}
