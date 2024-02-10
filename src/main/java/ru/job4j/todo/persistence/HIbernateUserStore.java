package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

/**
 * В данном классе мы применили шаблон
 * проектирования Command и упростили код.
 *
 * По сути в данном классе мы реализовали
 * абстрактную команду, а общее поведение,
 * характерное всем методам данного класса,
 * вынесено в {@link CrudRepository}.
 *
 * @author Constantine on 10.02.2024
 */
@AllArgsConstructor
@Repository
public class HIbernateUserStore implements UserStore {

    private final CrudRepository crudRepository;

    /**
     * Создать пользователя.
     *
     * Будет присвоен ID, сохранено в БД
     * имя, логин и пароль.
     */
    @Override
    public User save(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Найти пользователя по ID.
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "FROM User user WHERE user.login = :fLogin AND user.password = :fPassword", User.class,
                Map.of("fLogin", login,
                        "fPassword", password)
        );
    }
}
