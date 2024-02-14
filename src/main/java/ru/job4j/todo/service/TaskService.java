package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 22.01.2024
 */
public interface TaskService {

    Task create(Task task);

    void update(Task task);

    void delete(int taskId);

    Collection<Task> findAllOrderByDateTime(User user);

    Optional<Task> findById(int taskId);

    Collection<Task> findCompletedTasks(User user);

    Collection<Task> findNewTasks(User user);

    Collection<Task> findExpiredUncompletedTasks(User user);

    void complete(int id);
}
