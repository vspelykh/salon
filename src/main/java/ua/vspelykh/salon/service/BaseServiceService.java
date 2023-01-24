package ua.vspelykh.salon.service;

import ua.vspelykh.salon.dto.BaseServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface BaseServiceService {

    BaseService findById(Integer id) throws ServiceException;

    List<BaseServiceDto> findAll(String locale) throws ServiceException;

    void save(BaseService baseService) throws ServiceException;

    void delete(Integer baseServiceId) throws ServiceException;

    List<BaseService> findByFilter(String name, Integer priceFrom, Integer priceTo) throws ServiceException;

}
