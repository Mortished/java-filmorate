package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidatingService;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@WebMvcTest(controllers = FilmController.class)
@ContextConfiguration(classes = {FilmController.class})
class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ValidatingService validatingService;

    @Test
    void getAllFilms_positive() throws Exception {
        //given
        Film film = getDefaultFilm();
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));
        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.get("/films"));
        //then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(Matchers.hasSize(1))))
                .andExpect(jsonPath("$.[0].id", is(film.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(film.getName())))
                .andExpect(jsonPath("$.[0].description", is(film.getDescription())))
                .andExpect(jsonPath("$.[0].releaseDate", is(film.getReleaseDate().toString())))
                .andExpect(jsonPath("$.[0].duration", is(film.getDuration()), Long.class));
    }

    @Test
    void createFilm_positive() throws Exception {
        Film film = getDefaultFilm();
        var response = mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(film.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(film.getName())))
                .andExpect(jsonPath("$.description", is(film.getDescription())))
                .andExpect(jsonPath("$.releaseDate", is(film.getReleaseDate().toString())))
                .andExpect(jsonPath("$.duration", is(film.getDuration()), Long.class));
    }

    private Film getDefaultFilm() {
        return new Film(1L, "name", "CegjxX2tfX776lj3f2NY6Wll5KGRlTbPYecErJeCxlDx9NErGgKyhJ2DwtFRJKOBMFdRaGPaOiWKK0VMd9SD3WXmjx0gOHQtPwoN8jYgOw60V8tLiXMeoJ6ea1QXAdHwXLhlwldAPB9lHPraQoQlZqoQfrycZiGBBFNSyv18WuvayZZlWy75AF02pZBDmSXYhlmUvlZK", FILM_RELEASE_DATE, 100L);
    }
}