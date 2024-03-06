package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*,\n" +
                "       r.name as rating_name\n" +
                "FROM film f\n" +
                "         left join rating r on f.rating = r.id;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film save(Film film) {
        String sql = "insert into FILM(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            if (film.getMpa() != null) {
                stmt.setLong(5, film.getMpa().getId());
            } else {
                stmt.setObject(5, null);
            }
            return stmt;
        }, keyHolder);

        var id = keyHolder.getKey().longValue();
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            updateGenres(id, film.getGenres());
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());

        String sql = "UPDATE film\n" +
                "SET name         = ?,\n" +
                "    description  = ?,\n" +
                "    release_date = ?,\n" +
                "    duration     = ?,\n" +
                "    rating       = ?\n" +
                "WHERE id = ?;";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            deleteGenres(film.getId());
        } else {
            deleteGenres(film.getId());
            updateGenres(film.getId(), film.getGenres());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.*,\n" +
                "       r.name as rating_name\n" +
                "FROM film f\n" +
                "         join rating r on f.rating = r.id\n" +
                "WHERE f.id = ?;";
        var result = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        String sql = "select f.*, r.name as rating_name\n" +
                "from film f\n" +
                "         join rating r on f.rating = r.id\n" +
                "         left join favorite_films ff on f.id = ff.film_id\n" +
                "group by f.id\n" +
                "order by count(ff.film_id) desc\n" +
                "limit ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public List<Film> getRecommendations(Long userId) {

        String sqlQuery =
                "SELECT *, r.id, r.name as rating_name FROM film AS f " +
                        "JOIN rating AS r ON f.rating = r.id WHERE f.id IN (" +
                        "SELECT film_id FROM favorite_films WHERE user_id IN (" +
                        "SELECT ff.user_id FROM favorite_films AS ff " +
                        "LEFT JOIN favorite_films AS ff2 ON ff2.film_id = ff.film_id " +
                        "GROUP BY ff.user_id, ff2.user_id " +
                        "HAVING ff.user_id IS NOT NULL AND ff.user_id != ? AND ff2.user_id = ? " +
                        "ORDER BY COUNT(ff.user_id) DESC) AND film_id NOT IN (" +
                        "SELECT film_id FROM favorite_films WHERE user_id = ?))";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), userId, userId, userId);
    }

    public void likeFilm(Long userId, Long filmId) {
        String sql = "INSERT INTO favorite_films(user_id, film_id)" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    public void dislikeFilm(Long userId, Long filmId) {
        String sql = "DELETE FROM favorite_films WHERE user_id = ? and film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    private void updateGenres(Long filmId, List<Catalog> genres) {
        String secondSql = "INSERT INTO film_genre VALUES(?, ?);";
        genres.forEach(genreId -> jdbcTemplate.update(secondSql, filmId, genreId.getId()));
    }

    private void deleteGenres(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("duration"),
                new Catalog(rs.getLong("rating"), rs.getString("rating_name")),
                getFilmGenres(rs.getLong("id"))
        );
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

}
