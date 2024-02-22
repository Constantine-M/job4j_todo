package ru.job4j.todo.persistence.priority;

import ru.job4j.todo.model.Priority;

import java.util.Collection;

/**
 * @author Constantine on 18.02.2024
 */
public interface PriorityStore {

    Collection<Priority> findAll();
}
