package ru.job4j.todo.service.category;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;

/**
 * @author Constantine on 21.02.2024
 */
public interface CategoryService {

    Collection<Category> findAll();

    List<Category> findAllByIds(List<Integer> ids);
}
