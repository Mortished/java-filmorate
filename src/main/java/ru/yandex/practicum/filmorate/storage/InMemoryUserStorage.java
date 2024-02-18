package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> userStorage = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User save(User user) {
        return userStorage.put(user.getId(), user);
    }

    @Override
    public User update(User user) {
        if (!userStorage.containsKey(user.getId())) {
            throw new NotFoundException();
        }
        return userStorage.put(user.getId(), user);
    }

    @Override
    public User getUserById(Long id) {
        User result = userStorage.get(id);
        return result;
    }

    public List<User> getUserFriends(Long id) {
        List<User> result = new ArrayList<>();
        return result;
    }
}
