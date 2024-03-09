package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    Review getById(Long reviewId);

    List<Review> getReviewList(Long filmId, Long count);

    void likeReview(Long reviewId, Long userId);

    void dislikeReview(Long reviewId, Long userId);

    void deleteReviewLike(Long reviewId, Long userId);

    void deleteReviewDislike(Long reviewId, Long userId);

}
