package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class Review {

    private Long reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    @Positive
    private Long userId;
    @Positive
    private Long filmId;
    private Long useful;

}
