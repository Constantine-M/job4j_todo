package ru.job4j.todo.persistence.user;

import ru.job4j.todo.model.User;

import java.util.Optional;

/**
 * @author Constantine on 31.01.2024
 */
public interface UserStore {

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);
}
