package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

/**
 * @author Constantine on 31.01.2024
 */
@AllArgsConstructor
@Repository
public class HIbernateUserStore implements UserStore {

    private final SessionFactory sessionFactory;

    /**
     * Создать пользователя.
     *
     * Будет присвоен ID, сохранено в БД
     * имя, логин и пароль.
     */
    @Override
    public Optional<User> save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        }
    }

    /**
     * Найти пользователя по ID.
     *
     * Здесь, чтобы вернуть Optional<User>,
     * мы используем специальный метод,
     * определенный в {@link Query} -
     * {@link Query#uniqueResultOptional}.
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User user WHERE user.login = :fLogin AND user.password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            session.getTransaction().commit();
            return query.uniqueResultOptional();
        }
    }
}
