package ua.vspelykh.salon.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    private int discount;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;

}
