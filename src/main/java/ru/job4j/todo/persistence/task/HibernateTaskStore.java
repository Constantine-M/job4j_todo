package ru.job4j.todo.persistence.task;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.event.spi.PersistEventListener;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.CrudRepository;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Collection;
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
 */
@AllArgsConstructor
@Repository
public class HibernateTaskStore implements TaskStore {

    private final CrudRepository crudRepository;

    /**
     * Создать задачу.
     *
     * Данный метод добавляет модель
     * {@link Task} в persistent context.
     * По сути то же самое, что делает
     * {@link Session#save}, но есть нюансы.
     * {@link Session#persist} ничего не
     * возвращает, работает только в рамках
     * транзакции. Если вызвать метод у
     * detached-объекта, то будет выброшено
     * исключение {@link PersistenceException}.
     *
     * В Hibernate все построено на
     * event-ах и listener-ах.
     * Создается event, который обрабатывается
     * соответствующим listener-ом
     * {@link PersistEventListener#onPersist},
     * который собственно и выполнит команду
     * на добавление user-а в persistent
     * context.
     */
    @Override
    public Task create(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    /**
     * Обновить задачу в БД.
     *
     * {@link Session#update(Object)} переводит
     * состояние объекта из detached в
     * persistent.
     *
     * Столкнулся с Hibernate exception logged:
     * Update/delete queries cannot be typed.
     * В данном случае запрос должен быть нетипизированным
     * и заработает.
     *
     * {@link Session#merge} и {@link Session#update}
     * обновляют задачу полностью (все поля).
     * По ТЗ дата и время создания задачи не
     * должно изменяться. Поэтому в данном методе
     * был использован hql запрос.
     *
     * @param task задача.
     */
    @Override
    public void update(Task task) {
        String hql = """
                UPDATE Task as task
                SET task.title = :fTitle, task.description = :fDesc, task.priority = :fPriority
                WHERE task.id = :fId
                """;
        crudRepository.run(hql,
                Map.of("fTitle", task.getTitle(),
                        "fDesc", task.getDescription(),
                        "fId", task.getId(),
                        "fPriority", task.getPriority()
                )
        );
    }

    /**
     * Удалить задачу по ID.
     *
     * @param taskId ID задачи.
     */
    @Override
    public void delete(int taskId) {
        crudRepository.run("DELETE Task as task WHERE task.id = :fId",
                Map.of("fId", taskId)
        );
    }

    /**
     * Найти все задачи.
     *
     * Чтобы вывести список задач только залогиненого
     * пользователя, требуется его передать
     * в метод. Пользователя будем брать из сессии.
     *
     * JOIN FETCH в запросе позволяет сразу
     * загружать приоритет в БД. На текущий
     * момент он загружается не сразу, т.к.
     * мы с целью демонстрации возможностей Hibernate
     * выставили в {@link Priority}
     * стратегию ленивой загрузки - LAZY.
     *
     * @param user залогиненый пользователь
     * @return список задач, упорядоченный по времени.
     */
    @Override
    public Collection<Task> findAllOrderByDateTime(User user) {
        String hql = """
                    FROM Task task
                    JOIN FETCH task.priority LEFT JOIN FETCH task.categories
                    WHERE task.user = :fUser
                    ORDER BY task.created DESC
                    """;
        return crudRepository.query(hql, Task.class,
                Map.of("fUser", user));
    }

    /**
     * Найти задачу по ID.
     *
     * @param taskId ID задачи.
     * @return задача.
     */
    @Override
    public Optional<Task> findById(int taskId) {
        return crudRepository.optional(
                "FROM Task task JOIN FETCH task.priority WHERE task.id = :fId", Task.class,
                Map.of("fId", taskId)
        );
    }

    /**
     * Найти только выполненные задачи.
     *
     * @return список выполненных задач.
     */
    @Override
    public Collection<Task> findCompletedTasks(User user) {
        String hql = """
                    FROM Task task 
                    JOIN FETCH task.priority 
                    WHERE task.done = true 
                    AND task.user = :fUser
                    """;
        return crudRepository.query(hql, Task.class,
                Map.of("fUser", user));
    }

    /**
     * Найти список новых задач.
     *
     * Т.к. четкого определния новой задачи в ТЗ
     * нет, то я считаю новой задачей ту,
     * которой меньше 2 часов.
     *
     * Если задаче больше 2 часов, то
     * задача считается не новой.
     *
     * @return список новых задач.
     */
    @Override
    public Collection<Task> findNewTasks(User user) {
        String hql = """
                    FROM Task task
                    JOIN FETCH task.priority
                    WHERE task.created > :lastTime
                    AND task.user = :fUser
                    """;
        return crudRepository.query(hql, Task.class,
                Map.of("lastTime", LocalDateTime.now().minusHours(2),
                        "fUser", user)
        );
    }

    /**
     * Найти список невыполненных и
     * уже не новых задач.
     *
     * Если задаче больше 2 часов, то
     * задача считается не новой.
     *
     * @return список невыполненных и
     * уже не новых задач.
     */
    @Override
    public Collection<Task> findExpiredUncompletedTasks(User user) {
        String hql = """
                FROM Task task 
                JOIN FETCH task.priority
                WHERE task.created < :lastTime 
                AND task.done = false
                AND task.user = :fUser
                """;
        return crudRepository.query(hql, Task.class,
                Map.of("lastTime", LocalDateTime.now().minusHours(2),
                        "fUser", user)
        );
    }

    /**
     * Изменить состояние задачи на "выполнено".
     *
     * Метод пришлось переписать по аналогии
     * с методом удаления. Т.е. находим
     * задачу по ID и обновляем поле isDone
     * у {@link Task}.
     */
    @Override
    public void complete(int id) {
        String hql = """
                UPDATE Task as task
                SET task.done = :fDone
                WHERE task.id = :fId
                """;
        crudRepository.run(
                hql,
                Map.of("fId", id,
                        "fDone", true)
        );
    }
}
