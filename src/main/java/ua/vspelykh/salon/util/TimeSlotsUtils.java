package ua.vspelykh.salon.util;

import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ua.vspelykh.salon.util.exception.Messages.ERROR_CREATE_APPOINTMENT_ON_WEEKEND;


/**
 * Utility class with static methods for working with time slots and appointments.
 *
 * @version 1.0
 */
public class TimeSlotsUtils {

    private TimeSlotsUtils() {

    }

    /**
     * Returns a list of local times representing time slots between the given start and end times,
     * with a specified interval in minutes.
     *
     * @param timeStart the starting time of the time slots
     * @param timeEnd   the end time of the time slots
     * @param interval  the interval between time slots in minutes
     * @return a list of local times representing time slots
     */
    public static List<LocalTime> getSlots(LocalTime timeStart, LocalTime timeEnd, int interval) {
        List<LocalTime> slots = new ArrayList<>();
        slots.add(timeStart);
        while (timeStart.isBefore(timeEnd.minusMinutes(interval))) {
            timeStart = timeStart.plusMinutes(interval);
            slots.add(timeStart);
        }
        return slots;
    }

    /**
     * Removes time slots that are occupied by appointments from the given list of time slots.
     *
     * @param slots        the list of time slots to remove occupied slots from
     * @param appointments the list of appointments to check for occupied slots
     * @param interval     the interval between time slots in minutes
     */
    public static void removeOccupiedSlots(List<LocalTime> slots, List<Appointment> appointments, int interval) {
        for (Appointment appointment : appointments) {
            LocalTime startTime = appointment.getDate().toLocalTime();
            double intervalCount = (double) appointment.getContinuance() / (double) interval;
            removeProcess(intervalCount, startTime, interval, slots);
        }
    }

    /**
     * Removes slots from the given list of slots starting from the given start time, which are occupied by an appointment with
     * the given duration in minutes. The time interval between consecutive slots is given by the given interval.
     *
     * @param intervalCount the number of intervals required for the appointment duration
     * @param startTime     the start time of the appointment
     * @param interval      the time interval between consecutive slots in minutes
     * @param slots         the list of slots from which slots will be removed
     */
    private static void removeProcess(double intervalCount, LocalTime startTime, int interval, List<LocalTime> slots) {
        int countOfSlots = (int) Math.ceil(intervalCount);
        List<LocalTime> slotsForRemove = new ArrayList<>();
        slotsForRemove.add(startTime);
        LocalTime copy = LocalTime.of(startTime.getHour(), startTime.getMinute());
        for (int i = 0; i < countOfSlots - 1; i++) {
            copy = copy.plusMinutes(interval);
            slotsForRemove.add(copy);
        }
        slots.removeAll(slotsForRemove);
    }

    /**
     * Removes time slots that are occupied by appointment DTOs from the given list of time slots.
     *
     * @param slots        the list of time slots to remove occupied slots from
     * @param appointments the list of appointment DTOs to check for occupied slots
     * @param interval     the interval between time slots in minutes
     */
    public static void removeOccupiedSlotsForDtos(List<LocalTime> slots, List<AppointmentDto> appointments, int interval) {
        for (AppointmentDto appointment : appointments) {
            LocalTime startTime = appointment.getDate().toLocalTime();
            double d = (double) appointment.getContinuance() / (double) interval;
            removeProcess(d, startTime, interval, slots);
        }
    }

    /**
     * Counts the number of minutes available for an appointment at the given time, taking into account
     * the appointments already scheduled for the day.
     *
     * @param time         the starting time of the appointment
     * @param appointments the list of appointments already scheduled for the day
     * @param workingDay   the working day object representing the day of the appointment
     * @return the number of minutes available for the appointment
     * @throws ServiceException if the appointment is scheduled on a weekend day or if the working day is null
     */
    public static int countAllowedMinutes(Time time, List<Appointment> appointments, WorkingDay workingDay) throws ServiceException {
        if (workingDay == null) {
            throw new ServiceException(ERROR_CREATE_APPOINTMENT_ON_WEEKEND);
        }
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

    /**
     * Calculates the duration in minutes between a starting time and an ending time.
     *
     * @param start the starting time to calculate the duration from
     * @param end   the ending time to calculate the duration to
     * @return the duration in minutes as an integer
     */
    private static int getDuration(Time start, LocalTime end) {
        Duration duration;
        duration = Duration.between(start.toLocalTime(), end);
        return (int) (duration.getSeconds() / 60);
    }

    /**
     * Removes time slots from the given list if the date is today and the time has already passed.
     *
     * @param slots the list of time slots to be filtered
     * @param date  the date to be checked against the current date
     */
    public static void removeSlotsIfDateIsToday(List<LocalTime> slots, LocalDate date) {
        if (date.equals(LocalDate.now())) {
            slots.removeIf(slotTime -> LocalTime.now().isAfter(slotTime));
        }
    }
}
