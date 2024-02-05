package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 22.01.2024
 */
public interface TaskService {

    Task create(Task task);

    boolean update(Task task);

    boolean delete(int taskId);

    Collection<Task> findAllOrderByDateTime();

    Optional<Task> findById(int taskId);

    Collection<Task> findCompletedTasks();

    Collection<Task> findNewTasks();

    Collection<Task> findExpiredUncompletedTasks();

    boolean complete(int id);
}
