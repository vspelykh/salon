package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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
