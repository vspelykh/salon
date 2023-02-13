package ua.vspelykh.salon.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.model.entity.ServiceCategory;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

@Data
@EqualsAndHashCode(of = "id")
public class BaseServiceDto {

    private int id;
    private String category;
    private String service;
    private int price;

    public static class BaseServiceDtoBuilder{

        private final BaseService baseService;
        private final ServiceCategory category;
        private final String locale;

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
