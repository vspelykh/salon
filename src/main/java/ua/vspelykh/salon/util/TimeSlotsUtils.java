package ua.vspelykh.salon.util;

import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.WorkingDay;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeSlotsUtils {

    private TimeSlotsUtils() {

    }

    public static List<LocalTime> getSlots(LocalTime timeStart, LocalTime timeEnd, int interval) {
        List<LocalTime> slots = new ArrayList<>();
        slots.add(timeStart);
        while (timeStart.isBefore(timeEnd.minusMinutes(interval))) {
            timeStart = timeStart.plusMinutes(interval);
            slots.add(timeStart);
        }
        return slots;
    }

    public static void removeOccupiedSlots(List<LocalTime> slots, List<Appointment> appointments, int interval) {
        for (Appointment appointment : appointments) {
            LocalTime startTime = appointment.getDate().toLocalTime();
            double d = (double) appointment.getContinuance() / (double) interval;
            removeProcess(d, startTime, interval, slots);
        }
    }


    private static void removeProcess(double d, LocalTime startTime, int interval, List<LocalTime> slots) {
        int countOfSlots = (int) Math.ceil(d);
        List<LocalTime> slotsForRemove = new ArrayList<>();
        slotsForRemove.add(startTime);
        LocalTime copy = LocalTime.of(startTime.getHour(), startTime.getMinute());
        for (int i = 0; i < countOfSlots - 1; i++) {
            copy = copy.plusMinutes(interval);
            slotsForRemove.add(copy);
        }
        slots.removeAll(slotsForRemove);
    }

    public static void removeOccupiedSlotsForDtos(List<LocalTime> slots, List<AppointmentDto> appointments, int interval) {
        for (AppointmentDto appointment : appointments) {
            LocalTime startTime = appointment.getDate().toLocalTime();
            double d = (double) appointment.getContinuance() / (double) interval;
            removeProcess(d, startTime, interval, slots);
        }
    }

    public static int countAllowedMinutes(Time time, List<Appointment> appointments, WorkingDay workingDay) {
        if (appointments.isEmpty()) {
            return getDuration(time, workingDay.getTimeEnd());
        }
        appointments.sort(Comparator.comparing(Appointment::getDate));
        appointments.removeIf(a -> a.getDate().toLocalTime().isBefore(time.toLocalTime()));
        if (appointments.isEmpty()) {
            return getDuration(time, workingDay.getTimeEnd());
        }
        return getDuration(time, appointments.get(0).getDate().toLocalTime());
    }

    private static int getDuration(Time start, LocalTime end) {
        Duration duration;
        duration = Duration.between(start.toLocalTime(), end);
        return (int) (duration.getSeconds() / 60);
    }

    public static void removeSlotsIfDateIsToday(List<LocalTime> slots, LocalDate date) {
        if (date.equals(LocalDate.now())) {
            slots.removeIf(slotTime -> LocalTime.now().isAfter(slotTime));
        }
    }
}
