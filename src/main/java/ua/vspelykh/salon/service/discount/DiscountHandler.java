package ua.vspelykh.salon.service.discount;

import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DEFAULT_DISCOUNT;

/**
 * This class represents a handler for applying discounts to an appointment. It uses the chain of responsibility pattern
 * to apply discounts in a specific order. It first checks whether a birthday discount can be applied. If so, it applies
 * the discount and stops the chain. Otherwise, it checks whether a morning discount can be applied. If so, it applies
 * the discount and stops the chain. If no discounts can be applied, it sets the appointment's discount to 1.0, indicating
 * that there is no discount.
 * <p>
 * This class uses the Chain of Responsibility design pattern.
 *
 * @version 1.0
 */
public class DiscountHandler {

    private final List<DiscountStrategy> discounts;
    private final User client;
    private final LocalDateTime appointmentDate;

    /**
     * Constructs a new DiscountHandler for the given user and appointment date,
     * and applies all applicable discounts to the appointment.
     *
     * @param client          the user whose appointment to apply discounts to.
     * @param appointmentDate the date and time of the appointment.
     */
    public DiscountHandler(User client, LocalDateTime appointmentDate) {
        this.client = client;
        this.appointmentDate = appointmentDate;
        discounts = DiscountFactory.getAllDiscounts();
    }

    /**
     * Applies all applicable discounts to the appointment using the discounts
     * list, in the order in which they were added. If a discount can be applied,
     * its value is returned. If no discounts can be applied, the default discount
     * value of 1.0 is returned.
     *
     * @return the value of the applied discount, or the default discount value if no discounts can be applied.
     */
    public double getDiscount() {
        for (DiscountStrategy discount : discounts) {
            if (discount.canApplyDiscount(client, appointmentDate)) {
                return discount.getDiscount(client, appointmentDate);
            }
        }
        return DEFAULT_DISCOUNT;
    }

    public int getDiscountPercentage() {
        double coefficient = getDiscount();
        if (coefficient == DEFAULT_DISCOUNT) {
            return 0;
        }
        return (int) Math.abs((coefficient * 100) - 100);
    }
}


