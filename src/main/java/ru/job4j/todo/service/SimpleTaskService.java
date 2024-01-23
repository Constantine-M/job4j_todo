package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.persistence.TaskStore;

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
    public boolean update(Task task) {
        return taskStore.update(task);
    }

    @Override
    public boolean delete(int taskId) {
        var taskOptional = taskStore.findById(taskId);
        if (taskOptional.isEmpty()) {
            return false;
        }
        return taskStore.delete(taskId);
    }

    @Override
    public Collection<Task> findAllOrderByDateTime() {
        return taskStore.findAllOrderByDateTime();
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return taskStore.findById(taskId);
    }

    @Override
    public Collection<Task> findCompletedTasks() {
        return taskStore.findCompletedTasks();
    }

    @Override
    public Collection<Task> findNewTasks() {
        return taskStore.findNewTasks();
    }
}
