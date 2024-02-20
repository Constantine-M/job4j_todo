package ru.job4j.todo.service;

import ru.job4j.todo.model.Priority;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 18.02.2024
 */
public interface PriorityService {

    Collection<Priority> findAll();
}
