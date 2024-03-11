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
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private DirectorDbStorage directorDbStorage;

    @BeforeEach
    void prepareData() {
        directorDbStorage = new DirectorDbStorage(jdbcTemplate);
    }

    @Test
    public void testShouldReturnGetAllDirector() {
        var expected = List.of(getDefaultDirector());

        var result = directorDbStorage.getAll();
        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldReturnCreateDirector() {
        var expected = getDefaultDirector();
        var result = directorDbStorage.getById(expected.getId());

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldReturnUpdateDirector() {
        var first = getDefaultDirector();
        var updated = getDefaultDirector2();
        updated.setId(first.getId());

        directorDbStorage.update(updated);

        var result = directorDbStorage.getById(updated.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void testShouldReturnGetByIdDirector() {
        var expected = getDefaultDirector();
        var result = directorDbStorage.getById(expected.getId());

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldDeleteDirector() {
        var directors = getDefaultDirector();
        directorDbStorage.deleteById(directors.getId());
        var result = directorDbStorage.getAll();

        assertThat(result).isEmpty();
    }

    private Catalog getDefaultDirector() {
        Catalog catalog = new Catalog(1L, "director");
        Catalog savedDirector = directorDbStorage.save(catalog);
        return directorDbStorage.getById(savedDirector.getId());
    }

    private Catalog getDefaultDirector2() {
        Catalog catalog = new Catalog(1L, "newDirector");
        Catalog savedDirector = directorDbStorage.save(catalog);
        return directorDbStorage.getById(savedDirector.getId());
    }
}
