package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HIbernateUserStore;
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

    /**
     * Сохранить пользователя.
     *
     * В {@link HIbernateUserStore#save} просто
     * сохраняем пользователя и возвращаем его.
     *
     * В слое сервисов следует произвести проверку
     * прежде, чем сохранять пользователя в БД.
     * Так мы сохраним ранее сформированный функционал
     * и сможем пробросить текст с ошибкой в
     * наше представление (о том, что пользователь такой
     * уже существует например).
     *
     * @param user пользователь
     * @return Optional<User>
     */
    @Override
    public Optional<User> save(User user) {
        var userOptional = findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(userStore.save(user));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userStore.findByLoginAndPassword(login, password);
    }
}
