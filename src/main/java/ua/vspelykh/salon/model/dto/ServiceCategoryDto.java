package ua.vspelykh.salon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents data transfer object of the ServiceCategory entity.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ServiceCategoryDto {

    private int id;
    private String name;
}
