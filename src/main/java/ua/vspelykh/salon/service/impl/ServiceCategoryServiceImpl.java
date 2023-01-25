package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.ServiceCategoryDao;
import ua.vspelykh.salon.dto.ServiceCategoryDto;
import ua.vspelykh.salon.model.ServiceCategory;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private ServiceCategoryDao serviceCategoryDao = DaoFactory.getServiceCategoryDao();

    @Override
    public ServiceCategory findById(int id) throws ServiceException {
        try {
            return serviceCategoryDao.findById(id);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ServiceCategoryDto> findAll(String locale) throws ServiceException {
        try {
            List<ServiceCategoryDto> dtos = new ArrayList<>();
            for (ServiceCategory category : serviceCategoryDao.findAll()){
             dtos.add(toDto(category, locale));
            }
            return dtos;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private ServiceCategoryDto toDto(ServiceCategory category, String locale) {
        return new ServiceCategoryDto(category.getId(), "ua".equals(locale) ? category.getNameUa() : category.getName());
    }
}
