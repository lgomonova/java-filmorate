package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        getById(user.getId());
        return userStorage.update(user);
    }

    public User getById(long id) {
        return userStorage.getById(id).orElseThrow(() -> {
            log.warn("Пользователь с id = {} не найден", id);
            return new NotFoundException("Пользователь с id = " + id + " не найден");
        });
    }

    public void addFriend(long id, long friendId) {
        checkDifferent(id, friendId);
        User user = getById(id);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.info("Пользователи {} и {} теперь друзья", id, friendId);
    }

    public void removeFriend(long id, long friendId) {
        checkDifferent(id, friendId);
        User user = getById(id);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.info("Пользователи {} и {} больше не друзья", id, friendId);
    }

    public List<User> getFriends(long id) {
        return getById(id).getFriends().stream()
                .map(this::getById)
                .toList();
    }

    public List<User> getCommonFriends(long id, long otherId) {
        checkDifferent(id, otherId);
        User user = getById(id);
        User other = getById(otherId);
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(this::getById)
                .toList();
    }

    private void checkDifferent(long id, long otherId) {
        if (id == otherId) {
            log.warn("Передан один и тот же пользователь: {}", id);
            throw new ValidationException("Должны быть указаны разные пользователи");
        }
    }
}