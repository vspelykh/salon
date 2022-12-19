package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface BaseServiceService {

    List<BaseService> findAll() throws ServiceException;

    void save(BaseService baseService) throws ServiceException;

    void delete(Integer baseServiceId) throws ServiceException;

    List<BaseService> findByFilter(String name, Integer priceFrom, Integer priceTo) throws ServiceException;

}
