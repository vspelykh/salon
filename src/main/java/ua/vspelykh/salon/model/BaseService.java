package ua.vspelykh.salon.model;

public class BaseService extends AbstractBaseEntity {

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
