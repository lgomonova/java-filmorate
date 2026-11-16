package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    private User validUser() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

    @Test
    void validUserPasses() {
        assertDoesNotThrow(() -> controller.validate(validUser()));
    }

    @Test
    void nullEmailFails() {
        User user = validUser();
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emptyEmailFails() {
        User user = validUser();
        user.setEmail("");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emailWithoutAtFails() {
        User user = validUser();
        user.setEmail("mail.mail.ru");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void nullLoginFails() {
        User user = validUser();
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emptyLoginFails() {
        User user = validUser();
        user.setLogin("");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void loginWithSpaceFails() {
        User user = validUser();
        user.setLogin("log in");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emptyNameReplacedByLogin() {
        User user = validUser();
        user.setName("");
        controller.validate(user);
        assertEquals("login", user.getName());
    }

    @Test
    void nullNameReplacedByLogin() {
        User user = validUser();
        user.setName(null);
        controller.validate(user);
        assertEquals("login", user.getName());
    }

    @Test
    void birthdayTodayPasses() {
        User user = validUser();
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> controller.validate(user));
    }

    @Test
    void birthdayInFutureFails() {
        User user = validUser();
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emptyUserFails() {
        assertThrows(ValidationException.class, () -> controller.validate(new User()));
    }
}