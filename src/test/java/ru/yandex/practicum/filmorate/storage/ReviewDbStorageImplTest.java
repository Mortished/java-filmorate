package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.ReviewDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private ReviewDbStorageImpl reviewDbStorage;

    @BeforeEach
    void prepareData() {
        reviewDbStorage = new ReviewDbStorageImpl(jdbcTemplate);
        new UserDbStorageImpl(jdbcTemplate).save(getDefaultUser());
        new FilmDbStorageImpl(jdbcTemplate).save(getDefaultFilm());
    }

    @Test
    void create() {
        var expected = getDefaultReview();
        reviewDbStorage.create(expected);

        var result = reviewDbStorage.getById(expected.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void update() {
        var expected = getDefaultReview();
        reviewDbStorage.create(expected);
        var updated = getSecondReview();

        reviewDbStorage.update(updated);

        var result = reviewDbStorage.getById(updated.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    void delete() {
        var review = getDefaultReview();
        reviewDbStorage.create(review);

        var expectedMsg = "Данных с id = 1 не существует!";
        reviewDbStorage.delete(review.getReviewId());

        var result = assertThrows(NotExistException.class, () -> reviewDbStorage.getById(review.getReviewId()));
        assertEquals(expectedMsg, result.getMessage());
    }

    @Test
    void getByFilmId() {
        var review = List.of(getDefaultReview());
        review.forEach(reviewDbStorage::create);

        var result = reviewDbStorage.getReviewList(1L, 1L);
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    @Test
    void getAll() {
        var review = List.of(getDefaultReview());
        review.forEach(reviewDbStorage::create);

        var result = reviewDbStorage.getReviewList(-1L, 1L);
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    @Test
    void likeCheck() {
        var review = getDefaultReview();
        reviewDbStorage.create(review);

        reviewDbStorage.likeReview(review.getReviewId(), 1L);
        review.setUseful(1L);

        var result = reviewDbStorage.getById(review.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    @Test
    void dislikeCheck() {
        var review = getDefaultReview();
        reviewDbStorage.create(review);

        reviewDbStorage.dislikeReview(review.getReviewId(), 1L);
        review.setUseful(-1L);

        var result = reviewDbStorage.getById(review.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    @Test
    void removeLikeCheck() {
        var review = getDefaultReview();
        reviewDbStorage.create(review);

        reviewDbStorage.likeReview(review.getReviewId(), 1L);
        reviewDbStorage.deleteReviewLike(review.getReviewId(), 1L);

        var result = reviewDbStorage.getById(review.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    @Test
    void removeDislikeCheck() {
        var review = getDefaultReview();
        reviewDbStorage.create(review);

        reviewDbStorage.dislikeReview(review.getReviewId(), 1L);
        reviewDbStorage.deleteReviewLike(review.getReviewId(), 1L);

        var result = reviewDbStorage.getById(review.getReviewId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(review);
    }

    private Review getDefaultReview() {
        return new Review(1L, "content", Boolean.TRUE, 1L, 1L, 0L);
    }

    private Review getSecondReview() {
        return new Review(1L, "new content", Boolean.FALSE, 1L, 1L, 0L);
    }

    private User getDefaultUser() {
        return new User(1L, "test@email.ru", "login", "name", FILM_RELEASE_DATE);
    }

    private Film getDefaultFilm() {
        return new Film(1L, "name", "desc", FILM_RELEASE_DATE, 100L, null, null);
    }
}
