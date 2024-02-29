package ru.job4j.todo.util;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

/**
 * Данный утилитный класс описывает
 * работу с часовыми поясами.
 *
 * @author Constantine on 28.02.2024
 */
public class TimeZoneUtil {

    public static List<TimeZone> getAllAvailableTimeZones() {
        var timeZonesIds = TimeZone.getAvailableIDs();
        return Arrays.stream(timeZonesIds)
                .map(TimeZone::getTimeZone)
                .toList();
    }

    /**
     * Данный метод преобразует дату и время создания
     * задачи согласно часовому поясу пользователя.
     *
     * Храним в БД в "UTC", а выводим для
     * пользователя согласно его часовому поясу!
     *
     * Если часовой пояс не выбран, то время создания
     * задачи отображается по умолчанию в UTC Time zone.
     *
     * @param user залогиненый пользователь
     * @param tasks список задач залогиненого
     *              пользователя
     */
    public static void setDateTimeByUserTimezone(User user, Collection<Task> tasks) {
        if (user.getUserZone().isEmpty()) {
            user.setUserZone("UTC");
        } else {
            tasks.forEach(task -> task.setCreated(task.getCreated()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of(user.getUserZone()))
                    .toLocalDateTime()));
        }
    }
}
