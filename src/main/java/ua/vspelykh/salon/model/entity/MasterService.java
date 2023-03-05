package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The MasterService class This class represents a service that a master can provide, which includes
 * the duration (continuance) of the service.
 * It extends the AbstractBaseEntity class and includes additional fields for service details.
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
public class MasterService extends AbstractBaseEntity {

    private int masterId;
    private int baseServiceId;
    private int continuance;
}
