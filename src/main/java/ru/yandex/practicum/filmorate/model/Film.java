package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    private Long id;
    @NotBlank(message = "Необходимо указать имя")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма (duration) должна быть положительной")
    private Long duration;

    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration) {
        if (id == null) {
            this.id = IdGenerator.getInstance().getId();
        } else {
            this.id = id;
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
