package ru.job4j.todo.persistence.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CrudRepository;

import java.util.Collection;
import java.util.List;
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

    /**
     * Найти список категорий задачи.
     *
     * Метод принимает список ID категорий,
     * которые мы получим от клиента в
     * POST запросе.
     *
     * @param ids список ID категорий задачи.
     * @return список категорий {@link Category}.
     */
    @Override
    public List<Category> findAllByIds(List<Integer> ids) {
        return crudRepository.query("FROM Category category WHERE category.id IN :fIds", Category.class,
                Map.of("fIds", ids));
    }
}
