package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User save(User user);

    User update(User user);

    void removeUserById(Long id);

    User getUserById(Long id);

    List<User> getUserFriends(Long id);

    void addFriendship(Long userFrom, Long userTo);

    void removeFriendship(Long userFrom, Long userTo);
}
