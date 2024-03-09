package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Review {

    private Long reviewId;
    @NotNull(message = "Необходимо заполнить content")
    private String content;
    @NotNull(message = "Необходимо заполнить isPositive")
    private Boolean isPositive;
    @NotNull(message = "Необходимо заполнить userId")
    private Long userId;
    @NotNull(message = "Необходимо заполнить filmId")
    private Long filmId;
    private Long useful;

}
