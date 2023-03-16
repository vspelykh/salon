package ua.vspelykh.salon.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The BaseServiceDto class represents a data transfer object for MasterService entity.
 *
 * @version 1.0
 */
@Data
@EqualsAndHashCode
@Builder
@ToString
public class MasterServiceDto {

    private int id;
    private int masterId;
    private BaseServiceDto service;
    private int continuance;
}
