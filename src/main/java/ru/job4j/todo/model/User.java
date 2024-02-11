package ru.job4j.todo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Данный класс описывает модель пользователя.
 *
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
 *
 * @author Constantine on 31.01.2024
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "todo_user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String login;

    private String password;

    public User(int id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{"
                + "name='" + name + '\''
                +
                '}';
    }
}
