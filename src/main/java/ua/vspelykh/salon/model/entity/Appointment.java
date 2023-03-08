package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * The Appointment class represents an appointment entity in the application.
 * It extends the AbstractBaseEntity class and includes additional fields for appointment details.
 * <p>
 * Use the builder pattern to create new instances of this class.
 *
 * @version 1.0
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class Appointment extends AbstractBaseEntity {

    private Integer masterId;
    private Integer clientId;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private double discount;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;
}
