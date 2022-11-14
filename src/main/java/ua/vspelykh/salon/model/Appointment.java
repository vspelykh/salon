package ua.vspelykh.salon.model;

import java.time.LocalDateTime;

public class Appointment extends AbstractBaseEntity{

    private User master;
    private User client;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private int discount;

    public Appointment() {
    }

    public Appointment(Integer id, User master, User client, int continuance, LocalDateTime date, int price, int discount) {
        super(id);
        this.master = master;
        this.client = client;
        this.continuance = continuance;
        this.date = date;
        this.price = price;
        this.discount = discount;
    }
}
