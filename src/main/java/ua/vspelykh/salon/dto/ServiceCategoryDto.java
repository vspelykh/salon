package ua.vspelykh.salon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ServiceCategoryDto {

    private int id;
    private String name;
}
