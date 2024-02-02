package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "appointment_items")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AppointmentItem extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    @OneToOne
    @JoinColumn(name = "care_id", referencedColumnName = "id")
    private Care care;
}
