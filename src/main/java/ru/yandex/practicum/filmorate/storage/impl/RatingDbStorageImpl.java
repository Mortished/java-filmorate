package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.storage.CatalogStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingDbStorageImpl implements CatalogStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Catalog> getAll() {
        String sql = "SELECT * FROM rating;";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        String sql = "SELECT * FROM rating WHERE id = ?";
        var result = jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
