package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        getById(film.getId());
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        return filmStorage.getById(id).orElseThrow(() -> {
            log.warn("Фильм с id = {} не найден", id);
            return new NotFoundException("Фильм с id = " + id + " не найден");
        });
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        checkUserExists(userId);
        film.getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        checkUserExists(userId);
        film.getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк фильму {}", userId, filmId);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) {
            log.warn("Некорректное значение count = {}", count);
            throw new ValidationException("Параметр count должен быть больше нуля");
        }
        return filmStorage.getPopular(count);
    }

    private void checkUserExists(long userId) {
        if (userStorage.getById(userId).isEmpty()) {
            log.warn("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }
}