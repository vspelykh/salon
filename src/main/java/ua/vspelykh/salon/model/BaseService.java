package ua.vspelykh.salon.model;

import java.io.Serializable;

public class BaseService extends AbstractBaseEntity implements Serializable {

    private String service;
    private int price;

    public BaseService() {

    }

    public BaseService(Integer id, String service, int price) {
        super(id);
        this.service = service;
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
