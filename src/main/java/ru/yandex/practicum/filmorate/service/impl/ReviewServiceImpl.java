package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    public ReviewServiceImpl(ReviewStorage reviewStorage, UserService userService, FilmService filmService) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
        this.filmService = filmService;
    }

    @Override
    public Review create(@Valid Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        return reviewStorage.create(review);
    }

    @Override
    public Review update(@Valid Review review) {
        reviewStorage.getById(review.getReviewId());
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        return reviewStorage.update(review);
    }

    @Override
    public void delete(Long reviewId) {
        reviewStorage.delete(reviewId);
    }

    @Override
    public Review getById(Long reviewId) {
        return reviewStorage.getById(reviewId);
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
}
