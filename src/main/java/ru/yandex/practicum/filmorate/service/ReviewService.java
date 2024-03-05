package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import javax.validation.Valid;
import java.util.List;

public interface ReviewService {

    Review create(@Valid Review review);

    Review update(@Valid Review review);

    void delete(Long reviewId);

    Review getById(Long reviewId);

    List<Review> getReviewList(Long filmId, Long count);

    void likeReview(Long reviewId, Long userId);

    void dislikeReview(Long reviewId, Long userId);

    void deleteReviewLike(Long reviewId, Long userId);

    void deleteReviewDislike(Long reviewId, Long userId);
}
