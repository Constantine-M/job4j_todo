package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 18.02.2024
 */
@AllArgsConstructor
@Repository
public class HibernatePriorityStore implements PriorityStore {

    private final CrudRepository crudRepository;

    /**
     * Найти все приоритеты.
     *
     * @return список приоритетов.
     */
    @Override
    public Collection<Priority> findAll() {
        var priorityList = crudRepository.query("FROM Priority priority", Priority.class);
        return priorityList;
    }

    @Override
    public Optional<Priority> findById(int id) {
        return crudRepository.optional("FROM Priority priority WHERE priority.id = :fId", Priority.class,
                Map.of("fId", id));
    }
}
