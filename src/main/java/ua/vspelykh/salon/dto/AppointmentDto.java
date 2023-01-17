package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentDto {

    private User master;
    private User client;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private int discount;
    private List<BaseService> orderings;
}
