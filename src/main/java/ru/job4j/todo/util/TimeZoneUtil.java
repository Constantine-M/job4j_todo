package ru.job4j.todo.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Данный утилитный класс описывает
 * работу с часовыми поясами.
 *
 * @author Constantine on 28.02.2024
 */
@UtilityClass
public class TimeZoneUtil {

    public List<TimeZone> getAllAvailableTimeZones() {
        var timeZonesIds = TimeZone.getAvailableIDs();
        return Arrays.stream(timeZonesIds)
                .map(TimeZone::getTimeZone)
                .toList();
    }
}
