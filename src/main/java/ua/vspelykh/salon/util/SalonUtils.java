package ua.vspelykh.salon.util;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SalonUtils {

    public static Date getDate(String date){
        return Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    public static Time getTime(String time){
        return Time.valueOf(LocalTime.parse(time));
    }

    public static LocalDate getLocaleDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
