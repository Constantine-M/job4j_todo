package ru.job4j.todo.persistence.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.CrudRepository;

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
@Slf4j
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
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Method cant save user! Hibernate exception logged: {}", e.getMessage());
        }
        return Optional.empty();
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
