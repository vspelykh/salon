package ua.vspelykh.salon.model;

import java.time.LocalDateTime;

public class Appointment extends AbstractBaseEntity{

    private Integer masterId;
    private Integer clientId;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private int discount;

    public Appointment() {
    }

    public Appointment(Integer id, Integer masterId, Integer clientId, int continuance, LocalDateTime date, int price, int discount) {
        super(id);
        this.masterId = masterId;
        this.clientId = clientId;
        this.continuance = continuance;
        this.date = date;
        this.price = price;
        this.discount = discount;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public int getContinuance() {
        return continuance;
    }

    public void setContinuance(int continuance) {
        this.continuance = continuance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
