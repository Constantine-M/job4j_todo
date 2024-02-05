package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

/**
 * @author Constantine on 31.01.2024
 */
@Slf4j
@AllArgsConstructor
@Repository
public class HIbernateUserStore implements UserStore {

    private final SessionFactory sessionFactory;

    /**
     * Создать пользователя.
     *
     * Будет присвоен ID, сохранено в БД
     * имя, логин и пароль.
     *
     * В данном методе требуется обработка
     * исключения, т.к. в результате
     * неудачного выполнения запроса
     * можем поймать NPE.
     */
    @Override
    public Optional<User> save(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK WITH HIBERNATE EXCEPTION: {}", e.getMessage());
            session.getTransaction().rollback();
            return Optional.empty();
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
