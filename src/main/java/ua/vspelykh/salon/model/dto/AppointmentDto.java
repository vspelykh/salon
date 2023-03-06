package ua.vspelykh.salon.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The AppointmentDto class represents a data transfer object for an appointment in the application.
 * It includes information about the appointment, such as the master, client, date, price, and status.
 *
 * @version 1.0
 */
@Data
@EqualsAndHashCode(of = "id")
@Builder
public class AppointmentDto {

    private Integer id;
    private UserDto master;
    private UserDto client;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private double discount;
    private List<AppointmentItem> appointmentItems;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;
}
