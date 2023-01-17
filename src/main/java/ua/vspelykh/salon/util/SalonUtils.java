package ua.vspelykh.salon.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalonUtils {

    public static Date getDate(String date){
        return Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    public static LocalDate getLocaleDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
