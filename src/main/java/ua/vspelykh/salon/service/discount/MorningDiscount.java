package ua.vspelykh.salon.service.discount;

import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * An implementation of the DiscountStrategy interface that applies a discount
 * to an appointment if the appointment starts between 8:00 and 9:00 AM.
 * <p>
 * 5% discount for morning appointments.
 *
 * @version 1.0
 */
public class MorningDiscount implements DiscountStrategy {

    private static final double COEFFICIENT = 0.95;
    private static final LocalTime MORNING_START = LocalTime.of(8, 0);
    private static final LocalTime MORNING_END = LocalTime.of(9, 0);

    /**
     * Applies a 5% discount to appointments scheduled for the morning.
     *
     * @param client          the client who has scheduled the appointment.
     * @param appointmentDate the date and time of the appointment.
     * @return the discount coefficient to apply.
     */
    @Override
    public double getDiscount(User client, LocalDateTime appointmentDate) {
        return COEFFICIENT;
    }

    /**

     Determines if the given appointment is scheduled for the morning.
     @param client the client who has scheduled the appointment.
     @param appointmentDate the date and time of the appointment.
     @return true if the appointment is scheduled for the morning, false otherwise.
     */
    @Override
    public boolean canApplyDiscount(User client, LocalDateTime appointmentDate) {
        return isTimeInRange(appointmentDate.toLocalTime());
    }

    /**

     Determines if the given time is within the morning time frame.
     @param time the time to check.
     @return true if the given time is within the morning time frame, false otherwise.
     */
    private boolean isTimeInRange(LocalTime time) {
        return time.compareTo(MorningDiscount.MORNING_START) >= 0 && time.compareTo(MorningDiscount.MORNING_END) < 0;
    }
}