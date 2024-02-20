package ru.job4j.todo.service;

import ru.job4j.todo.model.Priority;
import ru.job4j.todo.persistence.PriorityStore;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 18.02.2024
 */
public interface PriorityService {

    Collection<Priority> findAll();

    Optional<Priority> findById(int id);
}
