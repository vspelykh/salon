package ua.vspelykh.salon.util;

import lombok.Builder;
import lombok.Data;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;

import java.time.LocalDate;

/**
 * Represents a filter object for querying appointments based on various criteria.
 *
 * @version 1.0
 */
@Builder
@Data
public class AppointmentFilter {

    private Integer masterId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;
}
