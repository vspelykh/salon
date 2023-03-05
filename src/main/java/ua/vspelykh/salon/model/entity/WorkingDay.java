package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The WorkingDay class represents a working day entity for a particular master in the application.
 * It extends the AbstractBaseEntity class and includes additional fields for the working day details.
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
public class WorkingDay extends AbstractBaseEntity {

    private Integer userId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
