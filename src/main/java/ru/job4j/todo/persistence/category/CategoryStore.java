package ru.job4j.todo.persistence.category;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;

/**
 * @author Constantine on 21.02.2024
 */
public interface CategoryStore {

    Collection<Category> findAll();

    Collection<Category> findAllByIds(List<Integer> ids);
}
