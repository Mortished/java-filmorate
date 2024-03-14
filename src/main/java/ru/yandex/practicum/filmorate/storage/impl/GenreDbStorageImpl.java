package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Catalog> getAll() {
        String sql = "SELECT * FROM genre;";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        String sql = "SELECT * FROM genre WHERE id = ?;";
        var result = jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    @Override
    public List<Catalog> getGenres(Long filmID) {

        Set<Catalog> genres = new HashSet<>(jdbcTemplate.query("SELECT g.id, g.name " + "FROM film_genre AS fg " + "LEFT OUTER JOIN genre AS g ON g.id = fg.genre_id " + "WHERE fg.film_id = ? " + "ORDER BY g.id", (rs, row) -> mapRow(rs), filmID));
        return new ArrayList<>(genres);
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(rs.getLong("id"), rs.getString("name"));
    }
}
