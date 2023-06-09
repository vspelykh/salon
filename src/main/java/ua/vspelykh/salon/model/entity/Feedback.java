package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * This class represents feedback left by a client after an appointment
 * The Feedback class extends the AbstractBaseEntity class and inherits the 'id' attribute from it.
 * <p>
 * Use the builder pattern to create new instances of this class. *
 *
 * @version 1.0
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class Feedback extends AbstractBaseEntity {

    private Integer appointmentId;
    private int mark;
    private String comment;
    private LocalDateTime date;
}
