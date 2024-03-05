package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    Review create(@RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    Review update(@RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    Review getById(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    @GetMapping
    List<Review> getReviewList(@RequestParam(defaultValue = "-1") Long filmId,
                               @RequestParam(defaultValue = "10") Long count) {
        return reviewService.getReviewList(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    void likeReview(@PathVariable Long id,
                    @PathVariable Long userId) {
        reviewService.likeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    void dislikeReview(@PathVariable Long id,
                       @PathVariable Long userId) {
        reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void deleteReviewLike(@PathVariable Long id,
                          @PathVariable Long userId) {
        reviewService.deleteReviewLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    void deleteReviewDislike(@PathVariable Long id,
                             @PathVariable Long userId) {
        reviewService.deleteReviewDislike(id, userId);
    }

}
