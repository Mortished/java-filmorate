package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private Long id;
    @NotNull(message = "Почта(email) не может быть пустой")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    @NotBlank(message = "Необходимо указать login")
    private String login;
    private String name;
    @NotNull
    @Past(message = "Дата рождения(birthday) не должна быть больше текущей")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

}
