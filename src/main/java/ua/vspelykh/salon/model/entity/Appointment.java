package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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
