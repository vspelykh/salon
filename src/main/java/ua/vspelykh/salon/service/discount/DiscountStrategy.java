package ua.vspelykh.salon.service.discount;

import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDateTime;

/**
 * An interface for applying discounts to an appointment.
 *
 * @version 1.0
 */
public interface DiscountStrategy {

    /**
     * Applies a discount to an appointment.
     *
     * @param client the client for whom the discount is being applied
     * @param appointmentDate the date and time of the appointment
     * @return the discount amount as a decimal value (e.g. 0.2 for 20% discount)
     */
    double getDiscount(User client, LocalDateTime appointmentDate);

    /**
     * Checks if the discount can be applied to the given appointment.
     *
     * @param client the client for whom the discount is being checked
     * @param appointmentDate the date and time of the appointment
     * @return true if the discount can be applied, false otherwise
     */
    boolean canApplyDiscount(User client, LocalDateTime appointmentDate);
}