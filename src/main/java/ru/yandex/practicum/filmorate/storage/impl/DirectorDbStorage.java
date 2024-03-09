package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Catalog> getAll() {
        String sqlQuery = "SELECT * FROM directors";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        try {
            String sqlQuery = "SELECT * FROM directors WHERE id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, (rs, row) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException(id.toString());
        }
    }

    @Override
    public Catalog save(Catalog catalog) {
        jdbcTemplate.update("INSERT INTO directors (name) VALUES (?)",
                catalog.getName());
        return jdbcTemplate.queryForObject(
                "SELECT id, name FROM directors WHERE id = " +
                        "(SELECT MAX(id) FROM directors)",
                (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog update(Catalog catalog) {
        String sqlQuery = "UPDATE directors SET name = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                catalog.getName(),
                catalog.getId());
        return getById(catalog.getId());
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        String sqlQuery = "DELETE FROM directors WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFilmDirectors(Long filmId, Set<Catalog> directors) {
        StringBuilder sb = new StringBuilder("Запрос на добавление в фильм с id = ")
                .append(filmId)
                .append(" режиссеров с id = ");
        List<Object[]> batch = new ArrayList<>();
        for (Catalog director : directors) {
            Object[] values = new Object[]{
                    filmId, director.getId()};
            sb.append(director.getId()).append(", ");
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)", batch);
    }

    @Override
    public void updateFilmDirectors(Long filmId, Set<Catalog> directors) {
        jdbcTemplate.update("DELETE FROM film_directors WHERE film_id = ?", filmId);
        addFilmDirectors(filmId, directors);
    }


    @Override
    public List<Catalog> getFilmDirectors(Long filmId) {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM directors WHERE id IN " +
                "(SELECT director_id FROM film_directors WHERE film_id = ?)", (rs, row) -> mapRow(rs), filmId));
    }

    @Override
    public List<Film> getDirectorFilms(Long directorId, String sortBy) {
        getById(directorId);
        if (sortBy.equals("year")) {
            return jdbcTemplate.query("SELECT f.*, " +
                            "r.name as mpa_name " +
                            "FROM film as f LEFT OUTER JOIN rating as r ON f.rating = r.id  " +
                            "WHERE f.id IN " +
                            "(SELECT DISTINCT film_id FROM film_directors WHERE director_id = ?) ORDER BY release_date",
                    (rs, rowNum) -> makeFilm(rs),
                    directorId);
        } else if (sortBy.equals("likes")) {
            return jdbcTemplate.query("SELECT f.*, " +
                            "r.name as mpa_name " +
                            "FROM film as f " +
                            "LEFT OUTER JOIN rating as r ON f.rating = r.id  " +
                            "LEFT JOIN (SELECT COUNT(user_id) AS likes_amount, film_id as likes_film_id " +
                            "FROM favorite_films GROUP BY film_id) AS fl " +
                            "ON f.id = fl.likes_film_id " +
                            "WHERE f.id IN " +
                            "(SELECT DISTINCT film_id FROM film_directors WHERE director_id = ?) " +
                            "ORDER BY likes_amount",
                    (rs, rowNum) -> makeFilm(rs),
                    directorId);
        }
        throw new ValidationException("Неизвестный тип сортировки");
    }

    private List<Catalog> getFilmGenres(Long filmId) {
        String sql = "SELECT * FROM genre g " +
                "WHERE id IN (SELECT genre_id FROM film_genre WHERE film_id = ?);";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), filmId);
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("duration"),
                new Catalog(rs.getLong("rating"), rs.getString("mpa_name")),
                getFilmGenres(rs.getLong("id")),
                getFilmDirectors(rs.getLong("id"))
        );
    }

}
