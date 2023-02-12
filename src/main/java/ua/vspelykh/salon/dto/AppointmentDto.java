package ua.vspelykh.salon.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.vspelykh.salon.model.AppointmentItem;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

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
    private int discount;
    private List<AppointmentItem> appointmentItems;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;

}
