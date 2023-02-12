package ua.vspelykh.salon.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class BaseService extends AbstractBaseEntity implements Serializable {

    private String service;
    private int categoryId;
    private String serviceUa;
    private int price;
}
