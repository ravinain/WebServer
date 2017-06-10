package org.eega.server.common;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Common Date Utility class.
 */
public final class DateUtils {

    /**
     * Utility class doesn't need to be instantiated.
     */
    private DateUtils(){}

    /**
     * Format input {@link LocalDateTime} object.
     * @param dateTime Input date time object.
     * @return Formatted date and time.
     */
    public static String convertDateToString(final LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Convert long milliseconds into date object and return the formatted date.
     * @param miliseconds Input date and time in long.
     * @return Formatted date and time.
     */
    public static String convertMilisecondsToStringDateTime(final long miliseconds) {
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(miliseconds), ZoneId.systemDefault());
        return convertDateToString(dateTime);
    }
}
