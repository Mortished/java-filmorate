package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.impl.EventDbStorage;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final EventDbStorage eventStorage;

    public ReviewServiceImpl(ReviewStorage reviewStorage, UserService userService, FilmService filmService,
                             EventDbStorage eventStorage) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
        this.filmService = filmService;
        this.eventStorage = eventStorage;
    }

    @Override
    public Review create(@Valid Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        reviewStorage.create(review);
        eventStorage.addEvent(new Event(review.getUserId(), EventType.REVIEW, EventOperation.ADD, review.getReviewId()));
        return review;
    }

    @Override
    public Review update(@Valid Review review) {
        reviewStorage.getById(review.getReviewId());
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());

        Review updated = reviewStorage.update(review);
        eventStorage.addEvent(new Event(updated.getUserId(), EventType.REVIEW, EventOperation.UPDATE, updated.getReviewId()));
        return updated;
    }

    @Override
    public void delete(Long reviewId) {
        Long userId = reviewStorage.getById(reviewId).getUserId();
        reviewStorage.delete(reviewId);
        eventStorage.addEvent(new Event(userId, EventType.REVIEW, EventOperation.REMOVE, reviewId));
    }

    @Override
    public Review getById(Long reviewId) {
        return reviewStorage.getById(reviewId);
    }

    @Override
    public List<Review> getReviewList(Long filmId, Long count) {
        if (filmId != -1L) {
            filmService.getFilmById(filmId);
        }
        return reviewStorage.getReviewList(filmId, count);
    }

    @Override
    public void likeReview(Long reviewId, Long userId) {
        reviewStorage.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        reviewStorage.dislikeReview(reviewId, userId);
    }

    @Override
    public void deleteReviewLike(Long reviewId, Long userId) {
        reviewStorage.deleteReviewLike(reviewId, userId);
    }

    @Override
    public void deleteReviewDislike(Long reviewId, Long userId) {
        reviewStorage.deleteReviewDislike(reviewId, userId);
    }
}
