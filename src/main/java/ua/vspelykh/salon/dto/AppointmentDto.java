package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentDto {

    private Integer id;
    private UserDto master;
    private UserDto client;
    private int continuance;
    private LocalDateTime date;
    private int price;
    private int discount;
    private List<Ordering> orderings;
    private AppointmentStatus status;

    public AppointmentDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDto getMaster() {
        return master;
    }

    public void setMaster(UserDto master) {
        this.master = master;
    }

    public UserDto getClient() {
        return client;
    }

    public void setClient(UserDto client) {
        this.client = client;
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

    public List<Ordering> getOrderings() {
        return orderings;
    }

    public void setOrderings(List<Ordering> orderings) {
        this.orderings = orderings;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
