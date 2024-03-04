package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.error.NotExistException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review create(Review review) {
        String sql = "insert into Reviews(CONTENT, POSITIVE, USER_ID, FILM_ID) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"review_id"});
                    stmt.setString(1, review.getContent());
                    stmt.setBoolean(2, review.getIsPositive());
                    stmt.setLong(3, review.getUserId());
                    stmt.setLong(4, review.getFilmId());
                    return stmt;
                },
                keyHolder);

        var id = keyHolder.getKey().longValue();
        review.setReviewId(id);
        review.setUseful(0L);
        return review;
    }

    @Override
    public Review update(Review review) {
        String sql = "UPDATE REVIEWS\n" +
                "SET CONTENT         = ?,\n" +
                "    POSITIVE  = ?,\n" +
                "    USER_ID = ?,\n" +
                "    FILM_ID     = ?\n" +
                "WHERE REVIEW_ID = ?;";

        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());

        return getById(review.getReviewId());
    }

    @Override
    public void delete(Long reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Review getById(Long reviewId) {
        String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        var result = jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), reviewId).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(reviewId.toString());
        }
        return result.get();
    }

    @Override
    public List<Review> getReviewList(Long filmId, Long count) {
        return null;
    }

    @Override
    public void likeReview(Long reviewId, Long userId) {

    }

    @Override
    public void dislikeReview(Long reviewId, Long userId) {

    }

    @Override
    public void deleteReviewLike(Long reviewId, Long userId) {

    }

    @Override
    public void deleteReviewDislike(Long reviewId, Long userId) {

    }

    private Review makeReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getLong("review_id"),
                rs.getString("content"),
                rs.getBoolean("positive"),
                rs.getLong("user_id"),
                rs.getLong("film_id"),
                rs.getLong("useful")
        );
    }
}
