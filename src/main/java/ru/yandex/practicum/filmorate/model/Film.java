package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;

}
