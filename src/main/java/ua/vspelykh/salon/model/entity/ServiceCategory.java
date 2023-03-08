package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * The ServiceCategory class represents a category of services offered in the salon.
 * It extends the AbstractBaseEntity class and includes additional fields for category details.
 * <p>
 * Use the builder pattern to create new instances of this class.
 *
 * @version 1.0
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceCategory extends AbstractBaseEntity {

    private String name;
    private String nameUa;
}
