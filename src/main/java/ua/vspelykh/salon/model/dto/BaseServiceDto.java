package ua.vspelykh.salon.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.model.entity.ServiceCategory;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

/**
 * The BaseServiceDto class represents a data transfer object for BaseService entity.
 * <p>
 * The class also has a nested builder class for creating BaseServiceDto instances based on BaseService,
 * ServiceCategory and Locale.
 *
 * @version 1.0
 */
@Data
@EqualsAndHashCode
@ToString
public class BaseServiceDto {

    private int id;
    private String category;
    private String service;
    private int price;

    /**
     * The BaseServiceDtoBuilder is a nested builder class that allows the creation of a BaseServiceDto
     * instance based on BaseService, ServiceCategory and Locale.
     */
    public static class BaseServiceDtoBuilder {

        private final BaseService baseService;
        private final ServiceCategory category;
        private final String locale;

        /**
         * Constructor for the BaseServiceDtoBuilder class.
         *
         * @param baseService the BaseService instance used to build the BaseServiceDto.
         * @param category    the ServiceCategory instance associated with the BaseService.
         * @param locale      the Locale used to localize the BaseServiceDto instance.
         */
        public BaseServiceDtoBuilder(BaseService baseService, ServiceCategory category, String locale) {
            this.baseService = baseService;
            this.category = category;
            this.locale = locale;
        }

        /**
         * Method that builds a new instance of the BaseServiceDto class based on the fields set in the builder.
         *
         * @return a new instance of the BaseServiceDto class.
         */
        public BaseServiceDto build() {
            BaseServiceDto dto = new BaseServiceDto();
            dto.setId(baseService.getId());
            dto.setService(baseService.getService());
            dto.setPrice(baseService.getPrice());
            if (UA_LOCALE.equals(locale)) {
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
