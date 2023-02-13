package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.ServiceCategoryDto;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface ServiceCategoryService {

    ServiceCategory findById(int id) throws ServiceException;

    List<ServiceCategoryDto> findAll(String locale) throws ServiceException;
}
