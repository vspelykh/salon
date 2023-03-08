package ua.vspelykh.salon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents data transfer object of the ServiceCategory entity.
 *
 * @version 1.0
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServiceCategoryDto {

    private int id;
    private String name;
}
