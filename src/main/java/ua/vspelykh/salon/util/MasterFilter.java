package ua.vspelykh.salon.util;

import lombok.Builder;
import lombok.Data;
import ua.vspelykh.salon.model.entity.MastersLevel;

import java.util.List;

/**
 * Represents a filter for searching masters.
 * <p>
 * Contains information about masters' levels, service IDs, category IDs, and search keywords.
 *
 * @version 1.0
 */
@Builder
@Data
public class MasterFilter {

    private List<MastersLevel> levels;
    private List<Integer> serviceIds;
    private List<Integer> categoriesIds;
    private String search;
}
