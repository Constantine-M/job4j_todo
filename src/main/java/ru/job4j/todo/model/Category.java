package ru.job4j.todo.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

/**
 * Модель описывает категорию задачи.
 *
 * Примеры категорий: "здоровье", "дом",
 * "покупки", "путешествия" и т.д.
 * У одной задачи может быть несколько
 * категорий.
 *
 * @author Constantine on 21.02.2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "categories")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Include
    private String name;
}
