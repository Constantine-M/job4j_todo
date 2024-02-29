package ru.job4j.todo.service.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.task.TaskStore;
import ru.job4j.todo.util.TimeZoneUtil;

import java.time.ZoneId;
import java.util.Collection;
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
 * @author Constantine on 22.01.2024
 */
@AllArgsConstructor
@Service
public class SimpleTaskService implements TaskService {

    private final TaskStore taskStore;

    @Override
    public Task create(Task task) {
        return taskStore.create(task);
    }

    @Override
    public void update(Task task) {
        taskStore.update(task);
    }

    @Override
    public void delete(int taskId) {
        taskStore.delete(taskId);
    }

    /**
     * Найти все задачи.
     *
     * Выводятся все задачи залогиненого
     * пользователя.
     *
     * @param user залогиненый в системе пользователь
     * @return список задач
     */
    @Override
    public Collection<Task> findAllOrderByDateTime(User user) {
        var tasks = taskStore.findAllOrderByDateTime(user);
        TimeZoneUtil.setDateTimeByUserTimezone(user, tasks);
        return tasks;
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return taskStore.findById(taskId);
    }

    @Override
    public Collection<Task> findCompletedTasks(User user) {
        var tasks = taskStore.findCompletedTasks(user);
        TimeZoneUtil.setDateTimeByUserTimezone(user, tasks);
        return tasks;
    }

    @Override
    public Collection<Task> findNewTasks(User user) {
        var tasks = taskStore.findNewTasks(user);
        TimeZoneUtil.setDateTimeByUserTimezone(user, tasks);
        return tasks;
    }

    @Override
    public Collection<Task> findExpiredUncompletedTasks(User user) {
        var expiredTasks = taskStore.findExpiredUncompletedTasks(user);
        TimeZoneUtil.setDateTimeByUserTimezone(user, expiredTasks);
        return expiredTasks;
    }

    @Override
    public void complete(int id) {
        taskStore.complete(id);
    }
}
