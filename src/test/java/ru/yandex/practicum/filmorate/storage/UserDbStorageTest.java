package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void getAll() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        var expected = List.of(getDefaultUser(), getSecondUser());
        expected.forEach(userDbStorage::save);

        var result = userDbStorage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void save() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        var expected = getDefaultUser();
        userDbStorage.save(expected);

        var result = userDbStorage.getUserById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void update() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        var user = getDefaultUser();
        userDbStorage.save(user);
        var updatedUser = getSecondUser();
        updatedUser.setId(user.getId());

        userDbStorage.update(updatedUser);

        var result = userDbStorage.getUserById(user.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    public void getById() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        var expected = getDefaultUser();
        userDbStorage.save(expected);

        var result = userDbStorage.getUserById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getUserFriendsAndAddFriend() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        var user = getDefaultUser();
        var friend = getSecondUser();

        userDbStorage.save(user);
        userDbStorage.save(friend);

        userDbStorage.addFriendship(user.getId(), friend.getId());

        var result = userDbStorage.getUserFriends(user.getId());
        assertThat(result)
                .isNotNull()
                .hasSize(1);
        assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(friend);
    }

    @Test
    public void removeFriend() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        var user = getDefaultUser();
        var friend = getSecondUser();

        userDbStorage.save(user);
        userDbStorage.save(friend);

        userDbStorage.addFriendship(user.getId(), friend.getId());

        userDbStorage.removeFriendship(user.getId(), friend.getId());
        var result = userDbStorage.getUserFriends(user.getId());

        assertThat(result).isEmpty();
    }


    private User getDefaultUser() {
        return new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2000-01-01"));
    }

    private User getSecondUser() {
        return new User(2L, "second@email.ru", "secondLogin", "secondName", LocalDate.parse("2010-01-01"));
    }

}