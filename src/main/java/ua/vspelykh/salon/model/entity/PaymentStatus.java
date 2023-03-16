package ua.vspelykh.salon.model.entity;

/**
 * The PaymentStatus enum represents the status of the payment for an appointment.
 * <p>
 * The possible payment statuses are:
 * PAID_BY_CARD: the appointment has been paid by card
 * PAID_IN_SALON: the appointment has been paid in the salon
 * NOT_PAID: the appointment has not been paid yet
 * RETURNED: the payment has been returned
 *
 * @version 1.0
 */
public enum PaymentStatus {

    PAID_BY_CARD, PAID_IN_SALON, NOT_PAID, RETURNED
}
