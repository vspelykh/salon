package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.ServiceCategory;

import static ua.vspelykh.salon.dao.mapper.Column.UA_LOCALE;

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

    public static class BaseServiceDtoBuilder{

        private BaseService baseService;
        private ServiceCategory category;
        private String locale;

        public BaseServiceDtoBuilder(BaseService baseService, ServiceCategory category, String locale) {
            this.baseService = baseService;
            this.category = category;
            this.locale = locale;
        }

        public BaseServiceDto build(){
            BaseServiceDto dto = new BaseServiceDto();
            dto.setId(baseService.getId());
            dto.setService(baseService.getService());
            if (UA_LOCALE.equals(locale)){
                dto.setService(baseService.getServiceUa());
                dto.setCategory(category.getNameUa());
            } else {
                dto.setService(baseService.getService());
                dto.setCategory(category.getName());
            }
            return dto;
        }
    }
}
