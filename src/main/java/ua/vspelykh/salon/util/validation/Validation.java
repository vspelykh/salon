package ua.vspelykh.salon.util.validation;

import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.TimeSlotsUtils;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalTime;
import java.util.List;

import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.INTERVAL;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.countAllowedMinutes;
import static ua.vspelykh.salon.util.TimeSlotsUtils.removeOccupiedSlots;

public class Validation {

    private static WorkingDayService workingDayService = ServiceFactory.getWorkingDayService();
    private static AppointmentService appointmentService = ServiceFactory.getAppointmentService();


    private Validation() {
    }

    public static void checkEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new IllegalArgumentException("email is not valid");
        }
    }

    public static void checkNumber(String number) {
        if (!number.matches("^\\+[0-9]{12}")) {
            throw new IllegalArgumentException("mobile number is not valid");
        }
    }

    public static void checkNameAndSurname(String name, String surname) {
        String regex = "[a-zA-Z]{2,25}|[а-яА-Я]{2,25}";
        if (!name.matches(regex) || !surname.matches(regex)) {
            throw new IllegalArgumentException("name and/or surname is not valid");
        }
    }

    public static void checkPassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,20}$")) {
            throw new IllegalArgumentException("password is not valid");
        }
    }

    public static void checkUser(User user) {
        checkEmail(user.getEmail());
        checkNumber(user.getNumber());
        checkNameAndSurname(user.getName(), user.getSurname());
    }

    public static boolean validateAppointment(Appointment appointment) throws ServiceException /*throws ValidationException*/ {
        WorkingDay day = workingDayService.getDayByUserIdAndDate(appointment.getMasterId(), appointment.getDate().toLocalDate());
        List<LocalTime> slots = TimeSlotsUtils.getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        List<Appointment> appointments = appointmentService.getByDateAndMasterId(day.getDate(), day.getUserId());
        removeOccupiedSlots(slots, appointments, INTERVAL);
        return countAllowedMinutes(getTime(String.valueOf(appointment.getDate().toLocalTime())), appointments, day)
                >= appointment.getContinuance() && slots.contains(appointment.getDate().toLocalTime());

    }
}
