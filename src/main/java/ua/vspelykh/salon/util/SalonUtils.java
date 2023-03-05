package ua.vspelykh.salon.util;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static ua.vspelykh.salon.controller.ControllerConstants.DATE_PATTERN;

/**
 * The SalonUtils class contains utility methods for manipulating dates and generating key strings.
 *
 * @version 1.0
 */
public class SalonUtils {

    private static final Random random = new Random();

    /**
     * Parses a date string in the format "dd-MM-yyyy" to a Date object.
     *
     * @param date the date string to parse
     * @return the corresponding Date object
     */
    public static Date getDate(String date) {
        return Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN)));
    }

    /**
     * Parses a time string in the format "HH:mm:ss" to a Time object.
     *
     * @param time the time string to parse
     * @return the corresponding Time object
     */
    public static Time getTime(String time) {
        return Time.valueOf(LocalTime.parse(time));
    }

    /**
     * Parses a date string in the format "dd-MM-yyyy" to a LocalDate object.
     *
     * @param date the date string to parse
     * @return the corresponding LocalDate object
     */
    public static LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Formats a LocalDate object as a string in the format "dd-MM-yyyy".
     *
     * @param date the LocalDate object to format
     * @return the formatted date string
     */
    public static String getStringDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Generates a random key string of length 15 containing letters and digits.
     *
     * @return the generated key string
     */
    public static String generateKeyString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private SalonUtils() {
    }
}
