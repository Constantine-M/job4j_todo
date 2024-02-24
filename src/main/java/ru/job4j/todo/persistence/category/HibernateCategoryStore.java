package ru.job4j.todo.persistence.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CrudRepository;

import java.util.Collection;
import java.util.Map;

/**
 * @author Constantine on 21.02.2024
 */
@AllArgsConstructor
@Repository
public class HibernateCategoryStore  implements CategoryStore {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("FROM Category category", Category.class);
    }

    @Override
    public Category findById(int id) {
        return crudRepository.optional("FROM Category category WHERE category.id = :fId", Category.class,
                Map.of("fId", id))
                .get();
    }
}
