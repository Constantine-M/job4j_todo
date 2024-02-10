package ru.job4j.todo.persistence;

import ru.job4j.todo.model.User;

import java.util.Optional;

/**
 * @author Constantine on 31.01.2024
 */
public interface UserStore {

    User save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);
}
