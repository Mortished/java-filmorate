package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@Service
@Validated
@Slf4j
public class ValidatingService {

    public void validateFilm(@Valid Film film) {
        if (film.getDescription().length() > 200) {
            ValidationException e = new ValidationException("Длинна описания (description) не может превышать 200 символов!");
            log.warn(e.getMessage());
            throw e;
        }
        if (film.getReleaseDate().isBefore(FILM_RELEASE_DATE)) {
            ValidationException e = new ValidationException("Дата релиза (releaseDate) не может быть раньше 28.12.1895");
            log.warn(e.getMessage());
            throw e;
        }
    }

    public void validateUser(@Valid User user) {
        if (user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            ValidationException e = new ValidationException("Логин не может быть пустым и содержать пробелы!");
            log.warn(e.getMessage());
            throw e;
        }
    }
}
