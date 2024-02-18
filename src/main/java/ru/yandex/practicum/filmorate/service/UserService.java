package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.utils.UserIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;

@Service
@Validated
@Slf4j
public class UserService {
    private final UserDbStorage userStorage;

    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(@Valid User user) {
        validateUser(user);
        userStorage.save(user);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, user);
        return user;
    }

    public User update(@Valid User user) {
        validateUser(user);
        userStorage.update(user);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        userStorage.addFriendship(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        userStorage.removeFriendship(id, friendId);
    }

    public List<User> getUserFriends(Long id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> collection = userStorage.getUserFriends(id);
        List<User> secondUser = userStorage.getUserFriends(otherId);
        collection.retainAll(secondUser);
        return collection;
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    private void validateUser(@Valid User user) {
        if (user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            ValidationException e = new ValidationException("Логин не может быть пустым и содержать пробелы!");
            log.warn(e.getMessage());
            throw e;
        }
        if (user.getId() == null) {
            user.setId(UserIdGenerator.getInstance().getId());
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
