package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Catalog;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorageImpl genreDbStorage;

    @BeforeEach
    void prepareData() {
        genreDbStorage = new GenreDbStorageImpl(jdbcTemplate);
    }

    @Test
    public void findAllGenres() {
        var expected = List.of(
                new Catalog(1L, "Комедия"),
                new Catalog(2L, "Драма"),
                new Catalog(3L, "Мультфильм"),
                new Catalog(4L, "Триллер"),
                new Catalog(5L, "Документальный"),
                new Catalog(6L, "Боевик")
        );
        var result = genreDbStorage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void findGenreById() {
        var expected = new Catalog(3L, "Мультфильм");

        var result = genreDbStorage.getById(3L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }


}
