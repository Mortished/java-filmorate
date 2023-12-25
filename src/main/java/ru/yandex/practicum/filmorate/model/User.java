package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    private Long id;
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    @NotBlank(message = "Необходимо указать login")
    private String login;
    private String name;
    @Past(message = "Дата рождения(birthday) не должна быть больше текущей")
    private LocalDate birthday;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        if (id == null) {
            this.id = IdGenerator.getInstance().getId();
        } else {
            this.id = id;
        }
        this.email = email;
        this.login = login;
        if (name == null) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

}
