package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
public class UserDbStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, row) -> makeUser(rs));
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users(email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        getUserById(user.getId());

        String sql = "UPDATE users\n" +
                "SET email         = ?,\n" +
                "    login  = ?,\n" +
                "    name = ?,\n" +
                "    birthday     = ?\n" +
                "WHERE id = ?;";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void removeUserById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        var result = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    @Override
    public List<User> getUserFriends(Long id) {
        getUserById(id);
        String sql = "SELECT *\n" +
                "FROM users\n" +
                "WHERE id IN (SELECT user_to FROM friendship WHERE user_from = ?);";
        return jdbcTemplate.query(sql, (rs, row) -> makeUser(rs), id);
    }

    public void addFriendship(Long userFrom, Long userTo) {
        String sql = "INSERT INTO friendship(user_from, user_to)" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userFrom, userTo);
    }

    public void removeFriendship(Long userFrom, Long userTo) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_FROM = ? AND USER_TO = ?";
        jdbcTemplate.update(sql, userFrom, userTo);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );
    }

}
