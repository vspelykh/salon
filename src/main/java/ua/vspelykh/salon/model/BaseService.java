package ua.vspelykh.salon.model;

import java.io.Serializable;

public class BaseService extends AbstractBaseEntity implements Serializable {

    private String service;
    private int categoryId;
    private String serviceUa;
    private int price;

    public BaseService() {

    }

    public BaseService(Integer id, String service, int categoryId, String serviceUa, int price) {
        super(id);
        this.service = service;
        this.categoryId = categoryId;
        this.serviceUa = serviceUa;
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
