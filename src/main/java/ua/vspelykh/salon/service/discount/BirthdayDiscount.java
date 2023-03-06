package ua.vspelykh.salon.service.discount;

import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A discount strategy that applies a discount to an appointment if the appointment date is the same
 * as the client's birthday.
 * <p>
 * 30% discount for appointments on the client's birthday
 *
 * @version 1.0
 */
public class BirthdayDiscount implements DiscountStrategy {

    private static final double COEFFICIENT = 0.7;


    /**
     * Applies a discount to an appointment if the appointment date is the same as the client's birthday.
     *
     * @param client          the client who made the appointment.
     * @param appointmentDate the date and time of the appointment.
     * @return the discount rate to be applied, which is 0.7 for appointments on the client's birthday, and 1.0 otherwise.
     */
    @Override
    public double getDiscount(User client, LocalDateTime appointmentDate) {
        return COEFFICIENT;
    }

    /**
     * Determines if the given appointment is eligible for a birthday discount.
     *
     * @param client          the client who made the appointment.
     * @param appointmentDate the date and time of the appointment.
     * @return true if the appointment is eligible for a birthday discount, false otherwise.
     */
    @Override
    public boolean canApplyDiscount(User client, LocalDateTime appointmentDate) {
        LocalDate birthdayOfUser = client.getBirthday();
        return isDatesEqual(appointmentDate.toLocalDate(), birthdayOfUser);
    }

    /**
     * Checks if date of appointment and birthday of client have the same month and day values.
     *
     * @param appointmentDate the first date to compare.
     * @param birthdayOfUser  the second date to compare.
     * @return true if the dates have the same month and day values, false otherwise.
     */
    private boolean isDatesEqual(LocalDate appointmentDate, LocalDate birthdayOfUser) {
        return appointmentDate.getMonth() == birthdayOfUser.getMonth()
                && appointmentDate.getDayOfMonth() == birthdayOfUser.getDayOfMonth();
    }
}
