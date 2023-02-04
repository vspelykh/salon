package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dto.BaseServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.ServiceCategory;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ua.vspelykh.salon.dao.mapper.Column.UA_LOCALE;

public class BaseServiceServiceImpl implements BaseServiceService {

    private static final Logger LOG = LogManager.getLogger(BaseServiceServiceImpl.class);

    private ServiceCategoryService serviceCategoryService;
    private BaseServiceDao baseServiceDao;

    @Override
    public BaseService findById(Integer id) throws ServiceException {
        try {
            return baseServiceDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findAll(String locale) throws ServiceException {
        try {
            return toDtos(baseServiceDao.findAll(), locale);
        } catch (DaoException e) {
            LOG.error("Error to get all base services");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(BaseService baseService) throws ServiceException {
        try {
            if (baseService.isNew()) {
                baseServiceDao.create(baseService);
            } else baseServiceDao.update(baseService);
        } catch (DaoException e) {
            LOG.error("Error to save base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer baseServiceId) throws ServiceException {
        try {
            baseServiceDao.removeById(baseServiceId);
        } catch (DaoException e) {
            LOG.error("Error to delete base service by id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findByFilter(List<Integer> categoriesIds, int page, int size, String locale) throws ServiceException {
        try {
            return toDtos(baseServiceDao.findByFilter(categoriesIds, page, size), locale);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    @Override
    public int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws ServiceException {
        try {
            return baseServiceDao.getCountOfCategories(categoriesIds, page, size);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private List<BaseServiceDto> toDtos(List<BaseService> baseServices, String locale) throws ServiceException {
        List<BaseServiceDto> dtos = new ArrayList<>();
        for (BaseService baseService : baseServices) {
            dtos.add(toDto(baseService, locale));
        }
        return dtos;
    }

    private BaseServiceDto toDto(BaseService baseService, String locale) throws ServiceException {
        BaseServiceDto dto = new BaseServiceDto();
        ServiceCategory category = serviceCategoryService.findById(baseService.getCategoryId());
        dto.setId(baseService.getId());
        if (Objects.equals(locale, UA_LOCALE)) {
            dto.setService(baseService.getServiceUa());
            dto.setCategory(category.getNameUa());
        } else {
            dto.setService(baseService.getService());
            dto.setCategory(category.getName());
        }
        dto.setPrice(baseService.getPrice());
        return dto;
    }

    public void setServiceCategoryService(ServiceCategoryService serviceCategoryService) {
        this.serviceCategoryService = serviceCategoryService;
    }

    public void setBaseServiceDao(BaseServiceDao baseServiceDao) {
        this.baseServiceDao = baseServiceDao;
    }
}
