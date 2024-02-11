package ru.job4j.todo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Чтобы наша энтити увидела таблицу,
 * с которой она связана, нужно в файле
 * конфигурации hibernate.cfg.xml
 * настроить маппинг.
 *
 * В связке Lombok + Hibernate имеются
 * подводные камни. Таким образом, в
 * entity-классах не нужно использовать
 * аннотации {@link lombok.Data},
 * {@link lombok.EqualsAndHashCode},
 * и {@link lombok.ToString}.
 *
 * Не наносят вреда entity-классам
 * аннотации {@link Getter}, {@link Setter},
 * {@link lombok.Builder}.
 */
@Table(name = "tasks")
@Entity
@NoArgsConstructor
@Getter
@Setter
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

    /**
     * Данное поле описывает время создания
     * объекта {@link Task} и по умолчанию
     * время = текущему времени.
     */
    private LocalDateTime created = LocalDateTime.now();

    private String title;

    private String description;

    /**
     * По умолчанию задача должна быть
     * незавершенной.
     */
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task(int id, LocalDateTime created, String title, String description, boolean done, User user) {
        this.id = id;
        this.created = created;
        this.title = title;
        this.description = description;
        this.done = done;
        this.user = user;
    }
}
