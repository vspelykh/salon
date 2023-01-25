package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.ServiceCategory;

public class BaseServiceDto {

    private int id;
    private String category;
    private String service;
    private int price;

    public BaseServiceDto(int id, String category, String service, int price) {
        this.id = id;
        this.category = category;
        this.service = service;
        this.price = price;
    }

    public BaseServiceDto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
