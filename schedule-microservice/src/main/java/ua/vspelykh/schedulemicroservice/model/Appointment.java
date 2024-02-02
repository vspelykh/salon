package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.vspelykh.schedulemicroservice.model.enums.PaymentStatus;
import ua.vspelykh.schedulemicroservice.model.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Appointment extends AbstractEntity {

    @Column(name = "master_id", nullable = false)
    private UUID masterId;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "continuance", nullable = false)
    private Integer continuance;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
}
