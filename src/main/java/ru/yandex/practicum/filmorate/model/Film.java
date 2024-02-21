package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {

    private Long id;
    @NotBlank(message = "Необходимо указать имя")
    private String name;
    @Size(min = 1, max = 200, message = "Длинна описания (description) не может превышать 200 символов!")
    private String description;
    @NotNull(message = "Дата релиза (releaseDate) не должна быть пустой")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма (duration) должна быть положительной")
    private Long duration;
    private Catalog mpa;
    private List<Catalog> genres;

}
