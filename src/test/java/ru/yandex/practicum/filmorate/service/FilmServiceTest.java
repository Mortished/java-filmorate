package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@SpringBootTest
class FilmServiceTest {

    @Autowired
    FilmService filmService;

    @Test
    void validateFilmPositive() {
        Film film = getDefaultFilm();

        assertDoesNotThrow(() -> filmService.create(film));
    }

    @Test
    void validateFilmNullName() {
        Film film = getDefaultFilm();
        film.setName(null);

        String expectedMsg = "Необходимо указать имя";

        var result = assertThrows(ConstraintViolationException.class, () -> filmService.create(film));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateFilmEmptyName() {
        Film film = getDefaultFilm();
        film.setName("");

        String expectedMsg = "Необходимо указать имя";

        var result = assertThrows(ConstraintViolationException.class, () -> filmService.create(film));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateFilmDescription() {
        Film film = getDefaultFilm();
        film.setDescription("CegjxX2tfX776lj3f2NY6Wll5KGRlTbPYecErJeCxlDx9NErGgKyhJ2DwtFRJKOBMFdRaGPaOiWKK0VMd9SD3WXmjx0gOHQtPwoN8jYgOw60V8tLiXMeoJ6ea1QXAdHwXLhlwldAPB9lHPraQoQlZqoQfrycZiGBBFNSyv18WuvayZZlWy75AF02pZBDmSXYhlmUvlZK1");

        String expectedMsg = "Длинна описания (description) не может превышать 200 символов!";

        var result = assertThrows(ConstraintViolationException.class, () -> filmService.create(film));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateFilmReleaseDate() {
        Film film = getDefaultFilm();
        film.setReleaseDate(FILM_RELEASE_DATE.minusDays(1));

        String expectedMsg = "Дата релиза (releaseDate) не может быть раньше 28.12.1895";

        var result = assertThrows(ValidationException.class, () -> filmService.create(film));
        assertEquals(expectedMsg, result.getMessage());
    }

    @Test
    void validateFilmDuration() {
        Film film = getDefaultFilm();
        film.setDuration(0L);

        String expectedMsg = "Продолжительность фильма (duration) должна быть положительной";

        var result = assertThrows(ConstraintViolationException.class, () -> filmService.create(film));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    private Film getDefaultFilm() {
        return new Film(1L, "name", "CegjxX2tfX776lj3f2NY6Wll5KGRlTbPYecErJeCxlDx9NErGgKyhJ2DwtFRJKOBMFdRaGPaOiWKK0VMd9SD3WXmjx0gOHQtPwoN8jYgOw60V8tLiXMeoJ6ea1QXAdHwXLhlwldAPB9lHPraQoQlZqoQfrycZiGBBFNSyv18WuvayZZlWy75AF02pZBDmSXYhlmUvlZK", FILM_RELEASE_DATE, 100L);
    }

    private String getErrorMessage(String fullMsg) {
        return fullMsg.split(":")[1].substring(1);
    }


}