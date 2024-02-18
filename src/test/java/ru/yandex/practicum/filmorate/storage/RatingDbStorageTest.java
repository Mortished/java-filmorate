package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Catalog;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void findAll() {
        var expected = List.of(
                new Catalog(1L, "G"),
                new Catalog(2L, "PG"),
                new Catalog(3L, "PG-13"),
                new Catalog(4L, "R"),
                new Catalog(5L, "NC-17")
        );
        RatingDbStorage ratingDbStorage = new RatingDbStorage(jdbcTemplate);
        var result = ratingDbStorage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void findRatingById() {
        var expected = new Catalog(5L, "NC-17");

        RatingDbStorage ratingDbStorage = new RatingDbStorage(jdbcTemplate);

        var result = ratingDbStorage.getById(5L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

}
