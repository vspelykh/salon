package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The AppointmentItem class represents an item on an appointment's invoice.
 * It extends the AbstractBaseEntity class and includes fields for the IDs of the appointment and the service
 * associated with the item.
 * <p>
 * Use the builder pattern to create new instances of this class or one of the constructors.
 *
 * @version 1.0
 */
@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppointmentItem extends AbstractBaseEntity {

    private Integer appointmentId;
    private Integer serviceId;

    public AppointmentItem(Integer appointmentId, Integer serviceId) {
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
    }

    public AppointmentItem(Integer id, Integer appointmentId, Integer serviceId) {
        super(id);
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
    }
}
