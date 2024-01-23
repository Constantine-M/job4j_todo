package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class HibernateTaskStore implements TaskStore {

    private final SessionFactory sessionFactory;

    /**
     * Создать задачу.
     *
     * В данном методе сипользуем
     * try-with-resources.
     */
    @Override
    public Task create(Task task) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            return task;
        }
    }

    /**
     * Обновить задачу в БД.
     *
     * Обновляются поля краткого описания
     * (title) и подробного описания
     * (description).
     *
     * В данном методе используем конструкцию
     * try-catch, чтобы в случае неудачного
     * обновления все изменения откатились
     * к первоначальным значениям, а ошибка
     * записалась в лог для последующего
     * изучения проблемы.
     *
     * @param task задача.
     */
    @Override
    public boolean update(Task task) {
        Session session = sessionFactory.openSession();
        int affectedRows = 0;
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                            "UPDATE Task as task SET task.title = :fTitle, task.description = :fDesc WHERE task.id = :fId", Task.class)
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDesc", task.getDescription())
                    .setParameter("fId", task.getId());
            affectedRows = query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK! Hibernate exception logged: {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return affectedRows > 0;
    }

    /**
     * Удалить задачу по ID.
     *
     * В данном методе используем конструкцию
     * try-catch, чтобы в случае неудачного
     * обновления все изменения откатились
     * к первоначальным значениям.
     *
     * @param taskId ID задачи.
     */
    @Override
    public boolean delete(int taskId) {
        Session session = sessionFactory.openSession();
        int affectedRows = 0;
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                            "DELETE Task as task WHERE task.id = :fId", Task.class)
                    .setParameter("fId", taskId);
            affectedRows = query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK! Hibernate exception logged: {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return affectedRows > 0;
    }

    /**
     * Найти все задачи.
     *
     * @return список задач, упорядоченный по времени.
     */
    @Override
    public Collection<Task> findAllOrderByDateTime() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task task ORDER BY task.created", Task.class);
            session.getTransaction().commit();
            return query.list();
        }
    }

    /**
     * Найти задачу по ID.
     *
     * Здесь, чтобы вернуть Optional<User>,
     * мы используем специальный метод,
     * определенный в {@link Query} -
     * {@link Query#uniqueResultOptional}.
     *
     * @param taskId ID задачи.
     * @return задача.
     */
    @Override
    public Optional<Task> findById(int taskId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                            "FROM Task task WHERE task.id = :fId", Task.class)
                    .setParameter("fId", taskId);
            session.getTransaction().commit();
            return query.uniqueResultOptional();
        }
    }

    /**
     * Найти только выполненные задачи.
     *
     * @return список выполненных задач.
     */
    @Override
    public Collection<Task> findCompletedTasks() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task task WHERE task.done = true", Task.class);
            session.getTransaction().commit();
            return query.list();
        }
    }

    /**
     * Найти список новых задач.
     *
     * Т.к. четкого определния новой задачи в ТЗ
     * нет, то я считаю новой задачей ту,
     * которой меньше суток.
     *
     * Если задаче больше 24 часов, то
     * задача считатся не новой.
     *
     * 240000 - это 24 часа в представлении
     * Hibernate (то что мне удалось найти).
     *
     * @return список новых задач.
     */
    @Override
    public Collection<Task> findNewTasks() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "FROM Task task WHERE (current_timestamp - task.created) > 240000", Task.class);
            session.getTransaction().commit();
            return query.list();
        }
    }
}
