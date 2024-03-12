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
import java.util.ArrayList;
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
        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            updateFilmDirectors(id, film.getDirectors());
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

        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {
            deleteFilmDirectors(film.getId());
        } else {
            deleteFilmDirectors(film.getId());
            updateFilmDirectors(film.getId(), film.getDirectors());
        }
        return getFilmById(film.getId());
    }

    @Override
    public void removeFilmById(Long id) {
        String sql = "DELETE FROM film WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
    public List<Film> getPopularFilms(Long count, Long genreId, Integer year) {
        List<Film> popularFilms;
        String yearFilter = "WHERE YEAR(f.release_date) = ? ";
        String genreFilter = "WHERE fg.genre_id = ? ";
        String genreJoin = "JOIN film_genre fg ON f.id = fg.film_id ";
        String genreAndYearFilter = "WHERE fg.genre_id = ? AND YEAR(f.release_date) = ? ";
        StringBuilder sql = new StringBuilder("SELECT f.*, r.name as rating_name\n" +
                "FROM film f JOIN rating r ON f.rating = r.id\n" +
                "LEFT JOIN favorite_films ff on f.id = ff.film_id ");
        String sqlEnd = "GROUP BY f.id ORDER BY count(ff.film_id) DESC LIMIT ?";

        if (genreId == null && year == null) {
            String sqlString = sql.append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeFilm(rs), count);
        } else if (genreId == null) {
            String sqlString = sql.append(yearFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeFilm(rs), year, count);
        } else if (year == null) {
            String sqlString = sql.append(genreJoin).append(genreFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeFilm(rs), genreId, count);
        } else {
            String sqlString = sql.append(genreJoin).append(genreAndYearFilter).append(sqlEnd).toString();
            popularFilms = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeFilm(rs), genreId, year, count);
        }
        return popularFilms;
    }

    @Override
    public List<Film> getRecommendations(Long userId) {

        String sqlQuery =
                "SELECT f.*, r.name as rating_name FROM film AS f " +
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


    @Override
    public List<Film> getFilmsByUser(Long userId) {
        String sql = "SELECT f.*, r.name AS rating_name\n" +
                "from FILM f\n" +
                "JOIN RATING R on f.RATING = R.ID\n" +
                "JOIN FAVORITE_FILMS ff on f.ID = ff.FILM_ID\n" +
                "WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), userId);
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

    private void updateFilmDirectors(Long filmId, List<Catalog> directors) {
        String sqlQuery = "INSERT INTO film_directors VALUES(?, ?);";
        directors.forEach(directorId -> jdbcTemplate.update(sqlQuery, filmId, directorId.getId()));
    }

    private void deleteFilmDirectors(Long filmId) {
        String sql = "DELETE FROM film_directors WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
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
                getFilmGenres(rs.getLong("id")),
                getFilmDirectors(rs.getLong("id"))
        );
    }

    private List<Catalog> getFilmDirectors(Long filmId) {
        String sqlQuery = "SELECT * FROM directors WHERE id IN " +
                "(SELECT director_id FROM film_directors WHERE film_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, (rs, row) -> mapRow(rs), filmId));
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
