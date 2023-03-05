package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Consultation extends AbstractBaseEntity {

    private String name;
    private String number;
    private LocalDateTime date;
}
