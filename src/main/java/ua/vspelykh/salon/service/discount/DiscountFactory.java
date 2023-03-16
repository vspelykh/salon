package ua.vspelykh.salon.service.discount;

import java.util.LinkedList;
import java.util.List;

/**
 * A factory class for creating instances of DiscountStrategy.
 *
 * @version 1.0
 */
public class DiscountFactory {

    private static final DiscountStrategy BIRTHDAY_DISCOUNT = new BirthdayDiscount();
    private static final DiscountStrategy MORNING_DISCOUNT = new MorningDiscount();

    /**
     * Returns a list of all available discounts.
     *
     * @return a list of DiscountStrategy instances.
     */
    public static List<DiscountStrategy> getAllDiscounts() {
        List<DiscountStrategy> discounts = new LinkedList<>();
        discounts.add(BIRTHDAY_DISCOUNT);
        discounts.add(MORNING_DISCOUNT);
        return discounts;
    }

    private DiscountFactory() {
    }
}
