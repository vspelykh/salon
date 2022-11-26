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
}
