package ru.job4j.todo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Чтобы наша энтити увидела таблицу,
 * с которой она связана, нужно в файле
 * конфигурации hibernate.cfg.xml
 * настроить маппинг.
 */
@Table(name = "tasks")
@Entity
@NoArgsConstructor
@Data
public class Task {

    /**
     * Данное поле является первичным ключом.
     *
     * GenerationType.IDENTITY - используется
     * встроенный в БД тип данных столбца
     * -identity - для генерации значения
     * первичного ключа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime created;

    private String title;

    private String description;

    private boolean done;
}
