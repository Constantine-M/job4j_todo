package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
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
     *
     * Чтобы данное поле не обновлялось во
     * время обновления задачи, мы использовали
     * параметр updatable.
     *
     * По умолчанию создается задача с
     * часовым поясом UTC - оно же абсолютное
     * время.
     */
    @Column(updatable = false)
    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

    private String title;

    private String description;

    /**
     * По умолчанию задача должна быть
     * незавершенной.
     */
    private boolean done;

    /**
     * updatable=false выставить не удастся -
     * выпадает предупреждение/ошибка
     * "@Column(s) not allowed on a @ManyToOne property".
     * Поэтому в методе обновления мы сетим
     * юзера заново, иначе он зануляется.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * В данном проекте мы использовали
     * стратегию ленивой загрузки для
     * демонстрации решения вытекающей
     * проблемы {@link org.hibernate.LazyInitializationException}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    /**
     * В проекте будет односторонняя связь
     * ManyToMany между задачами и категориями.
     *
     * Главным в этой связи будет Task. Т.е.
     * при удалении Task, удаляется также и
     * связь между Task и промежуточной таблицей,
     * а категория и ее связь с промежуточной
     * таблицей остается.
     *
     * {@link Task} - это родительский объект
     * (joinColumns)
     * {@link Category} - это объект, который загружаем
     * в Task (inverseJoinColumns)
     */
    @ManyToMany
    @JoinTable(
            name = "task_category",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Category> categories = new ArrayList<>();
}
