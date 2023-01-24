package ua.vspelykh.salon.model;

import java.io.Serializable;

public class BaseService extends AbstractBaseEntity implements Serializable {

    private String service;
    private String serviceUa;
    private int price;

    public BaseService() {

    }

    public BaseService(Integer id, String service, String serviceUa, int price) {
        super(id);
        this.service = service;
        this.serviceUa = serviceUa;
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceUa() {
        return serviceUa;
    }

    public void setServiceUa(String serviceUa) {
        this.serviceUa = serviceUa;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
