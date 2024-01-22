package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskStore {

    Task create(Task task);

    void update(Task task);

    void delete(int taskId);

    Collection<Task> findAllOrderByDateTime();

    Optional<Task> findById(int taskId);
}
