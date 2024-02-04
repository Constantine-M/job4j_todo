package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.UserStore;

import java.util.Optional;

/**
 * В сервисе мы просто вызываем методы
 * репозитория, потому что вся логика
 * приложения сводится к логике работы
 * с данными.
 *
 * Все взаимодействия в программе производятся
 * через интерфейсы.
 *
 * @author Constantine on 31.01.2024
 */
@AllArgsConstructor
@Service
public class SimpleUserService implements UserService {

    private final UserStore userStore;

    @Override
    public Optional<User> save(User user) {
        return userStore.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userStore.findByLoginAndPassword(login, password);
    }
}
