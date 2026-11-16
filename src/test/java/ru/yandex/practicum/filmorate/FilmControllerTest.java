package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
    }

    private Film validFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);
        return film;
    }

    @Test
    void validFilmPasses() {
        assertDoesNotThrow(() -> controller.validate(validFilm()));
    }

    @Test
    void nullNameFails() {
        Film film = validFilm();
        film.setName(null);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void emptyNameFails() {
        Film film = validFilm();
        film.setName("");
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void blankNameFails() {
        Film film = validFilm();
        film.setName("   ");
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void description200Passes() {
        Film film = validFilm();
        film.setDescription("a".repeat(200));
        assertDoesNotThrow(() -> controller.validate(film));
    }

    @Test
    void description201Fails() {
        Film film = validFilm();
        film.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void releaseDateOnBoundaryPasses() {
        Film film = validFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> controller.validate(film));
    }

    @Test
    void releaseDateBeforeBoundaryFails() {
        Film film = validFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void nullReleaseDateFails() {
        Film film = validFilm();
        film.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void durationOnePasses() {
        Film film = validFilm();
        film.setDuration(1);
        assertDoesNotThrow(() -> controller.validate(film));
    }

    @Test
    void durationZeroFails() {
        Film film = validFilm();
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void negativeDurationFails() {
        Film film = validFilm();
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void emptyFilmFails() {
        assertThrows(ValidationException.class, () -> controller.validate(new Film()));
    }
}