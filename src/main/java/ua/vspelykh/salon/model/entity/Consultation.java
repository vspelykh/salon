package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * The Consultation class represents a consultation request made by a client.
 * It extends the AbstractBaseEntity class and includes fields for the client's name, phone number,
 * and requested date of the consultation.
 * <p>
 * Use the builder pattern to create new instances of this class.
 *
 * @version 1.0
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class Consultation extends AbstractBaseEntity {

    private String name;
    private String number;
    private LocalDateTime date;
    private boolean isRead;
}
