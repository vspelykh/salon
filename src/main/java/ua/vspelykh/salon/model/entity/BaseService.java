package ua.vspelykh.salon.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * The BaseService class represents a service offered by the salon.
 * It extends the AbstractBaseEntity class and includes fields for the name of the service, the ID of the
 * category to which it belongs, the Ukrainian translation of the service name, and the price of the service.*
 * <p>
 * Use the builder pattern, to create new instances of this class.
 *
 * @version 1.0
 */
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class BaseService extends AbstractBaseEntity {

    private String service;
    private int categoryId;
    private String serviceUa;
    private int price;
}
