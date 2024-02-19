package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.utils.DefaultData.FILM_RELEASE_DATE;

@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserServiceImpl userServiceImpl;

    private String getErrorMessage(String fullMsg) {
        return fullMsg.split(":")[1].substring(1);
    }

    @Test
    void validateUserPositive() {
        User user = getDefaultUser();

        assertDoesNotThrow(() -> userServiceImpl.create(user));
    }

    @Test
    void validateUserEmail() {
        User user = getDefaultUser();
        user.setEmail("123");

        String expectedMsg = "Email должен быть корректным адресом электронной почты";

        var result = assertThrows(ConstraintViolationException.class, () -> userServiceImpl.create(user));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateUserLoginNull() {
        User user = getDefaultUser();
        user.setLogin(null);

        String expectedMsg = "Необходимо указать login";

        var result = assertThrows(ConstraintViolationException.class, () -> userServiceImpl.create(user));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateUserLoginEmpty() {
        User user = getDefaultUser();
        user.setLogin("");

        String expectedMsg = "Необходимо указать login";

        var result = assertThrows(ConstraintViolationException.class, () -> userServiceImpl.create(user));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateUserLoginWithWhitespace() {
        User user = getDefaultUser();
        user.setLogin("1 1");

        String expectedMsg = "Логин не может быть пустым и содержать пробелы!";

        var result = assertThrows(ValidationException.class, () -> userServiceImpl.create(user));
        assertEquals(expectedMsg, result.getMessage());
    }

    @Test
    void validateUserBirthday() {
        User user = getDefaultUser();
        user.setBirthday(LocalDate.of(2090, 1, 1));

        String expectedMsg = "Дата рождения(birthday) не должна быть больше текущей";

        var result = assertThrows(ConstraintViolationException.class, () -> userServiceImpl.create(user));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    private User getDefaultUser() {
        return new User(1L, "test@email.ru", "login", "name", FILM_RELEASE_DATE);
    }

}
