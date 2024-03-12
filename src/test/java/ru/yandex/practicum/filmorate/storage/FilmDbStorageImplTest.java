package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorageImpl storage;
    private DirectorDbStorage directorDbStorage;

    @BeforeEach
    void prepareData() {
        storage = new FilmDbStorageImpl(jdbcTemplate);
        directorDbStorage = new DirectorDbStorage(jdbcTemplate);
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
    public void remove() {
        var expected = List.of(getDefaultFilm());
        var second = getSecondFilm();
        expected.forEach(storage::save);
        storage.save(second);

        storage.removeFilmById(second.getId());

        var result = storage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(expected);
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

        Film savedFilm = storage.save(getDefaultFilm());
        Film secondSavedFilm = storage.save(getSecondFilm());
        userDbStorage.save(new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2000-01-01")));
        storage.likeFilm(1L, 1L);

        var expected = List.of(savedFilm, secondSavedFilm);
        var result = storage.getPopularFilms(10L, null, null);

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getPopularFilmListOfUserAndFriend() {
        UserDbStorageImpl userDbStorage = new UserDbStorageImpl(jdbcTemplate);

        userDbStorage.save(getDefaultUser());
        userDbStorage.save(getDefaultSecondUser());
        Film savedFilm = storage.save(getDefaultFilm());
        Film secondSavedFilm = storage.save(getSecondFilm());
        storage.likeFilm(1L, 1L);
        storage.likeFilm(2L, 2L);
        storage.likeFilm(1L, 2L);
        userDbStorage.addFriendship(1L, 2L);

        var expected = List.of(savedFilm, secondSavedFilm);
        var result = storage.getFilmsByUser(1L);

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

    private User getDefaultUser() {
        return new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2000-01-01"));
    }

    private User getDefaultSecondUser() {
        return new User(2L, "second@email.ru", "secondLogin", "secondName", LocalDate.parse("2005-09-14"));
    }

    @Test
    void getRecommendations() {

        UserDbStorageImpl userStorage = new UserDbStorageImpl(jdbcTemplate);

        userStorage.save(new User(1L, "mail@ya.ru",
                "user01", "dude", LocalDate.of(1991, 1, 1)));

        userStorage.save(new User(2L, "another@mail.ru",
                "user02", "mikey", LocalDate.of(1991, 1, 1)));

        storage.save(getDefaultFilm());
        storage.save(getSecondFilm());

        storage.likeFilm(1L, 1L);
        storage.likeFilm(2L, 1L);
        storage.likeFilm(1L, 2L);

        List<Film> films = storage.getRecommendations(2L);

        assertThat(films)
                .isNotNull()
                .hasSize(films.size())
                .usingRecursiveComparison()
                .isEqualTo(List.of(getSecondFilm()));
    }

    private Film getDefaultFilm() {
        return new Film(1L, "firstFilm", "firstDescription", LocalDate.parse("2000-12-12"),
                122L, getDefaultMpa(), List.of(getDefaultGenre()), List.of(getDefaultDirector()));
    }

    private Film getSecondFilm() {
        return new Film(2L, "secondFilm", "secondDescription", LocalDate.parse("2010-01-01"),
                99L, new Catalog(1L, "G"), List.of(new Catalog(3L, "Мультфильм")),
                List.of(new Catalog(1L, "director")));
    }

    private Catalog getDefaultMpa() {
        return new Catalog(3L, "PG-13");
    }

    private Catalog getDefaultGenre() {
        return new Catalog(2L, "Драма");
    }

    private Catalog getDefaultDirector() {
        Catalog catalog = new Catalog(1L, "director");
        Catalog savedDirector = directorDbStorage.save(catalog);
        return directorDbStorage.getById(savedDirector.getId());
    }

}
