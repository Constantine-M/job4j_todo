package ru.job4j.todo.service.priority;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.persistence.priority.PriorityStore;

import java.util.Collection;

/**
 * @author Constantine on 18.02.2024
 */
@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {

    private final PriorityStore priorityStore;

    @Override
    public Collection<Priority> findAll() {
        return priorityStore.findAll();
    }
}
