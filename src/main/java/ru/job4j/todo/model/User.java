package ru.job4j.todo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Данный класс описывает модель пользователя.
 *
 * Чтобы наша энтити увидела таблицу,
 * с которой она связана, нужно в файле
 * конфигурации hibernate.cfg.xml
 * настроить маппинг.
 *
 * @author Constantine on 31.01.2024
 */
@NoArgsConstructor
@Entity
@Table(name = "todo_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String login;

    private String password;
}
