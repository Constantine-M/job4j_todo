package ru.job4j.todo.service.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.category.CategoryStore;

import java.util.Collection;
import java.util.List;

/**
 * @author Constantine on 21.02.2024
 */
@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    private final CategoryStore categoryStore;

    @Override
    public Collection<Category> findAll() {
        return categoryStore.findAll();
    }

    @Override
    public List<Category> findAllByIds(List<Integer> ids) {
        return categoryStore.findAllByIds(ids);
    }
}
