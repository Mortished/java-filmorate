package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageImplTest {

    private final static String FILM_SQL = "ALTER TABLE film ALTER COLUMN id RESTART WITH 1";
    private final static String USERS_SQL = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1";
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorageImpl storage;

    @BeforeEach
    void prepareData() {
        storage = new FilmDbStorageImpl(jdbcTemplate);
        jdbcTemplate.update(FILM_SQL);
        jdbcTemplate.update(USERS_SQL);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "film_genre", "favorite_films", "film", "users");
    }

    @Test
    public void getAll() {
        var expected = List.of(getDefaultFilm(), getSecondFilm());
        expected.forEach(storage::save);

        var result = storage.getAll();
        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void create() {
        var expected = getDefaultFilm();

        storage.save(expected);
        var result = storage.getFilmById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void update() {
        var first = getDefaultFilm();
        var updated = getSecondFilm();
        updated.setId(first.getId());
        storage.save(first);

        storage.update(updated);

        var result = storage.getFilmById(updated.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void getById() {
        var expected = getDefaultFilm();
        storage.save(expected);

        var result = storage.getFilmById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getPopularFilms() {
        UserDbStorageImpl userDbStorage = new UserDbStorageImpl(jdbcTemplate);

        storage.save(getDefaultFilm());
        storage.save(getSecondFilm());
        userDbStorage.save(new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2000-01-01")));
        storage.likeFilm(1L, 2L);

        var expected = List.of(getSecondFilm(), getDefaultFilm());
        var result = storage.getPopularFilms(10L);

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private Film getDefaultFilm() {
        return new Film(1L, "firstFilm", "firstDescription", LocalDate.parse("2000-12-12"),
                122L, getDefaultMpa(), List.of(getDefaultGenre()));
    }

    private Film getSecondFilm() {
        return new Film(2L, "secondFilm", "secondDescription", LocalDate.parse("2010-01-01"),
                99L, new Catalog(1L, "G"), List.of(new Catalog(3L, "Мультфильм")));
    }

    private Catalog getDefaultMpa() {
        return new Catalog(3L, "PG-13");
    }

    private Catalog getDefaultGenre() {
        return new Catalog(2L, "Драма");
    }

}