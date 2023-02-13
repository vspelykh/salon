package ua.vspelykh.salon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class MasterServiceDto {

    private int id;
    private int masterId;
    private BaseServiceDto service;
    private int continuance;
}
