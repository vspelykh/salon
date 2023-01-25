package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dao.Dao;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dto.BaseServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.ServiceCategory;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseServiceServiceImpl implements BaseServiceService {

    private static final Logger LOG = LogManager.getLogger(BaseServiceServiceImpl.class);

    private static final Dao<ServiceCategory> scs = DaoFactory.getServiceCategoryDao();
    private final BaseServiceDao bsDao;

    public BaseServiceServiceImpl() {
        this.bsDao = DaoFactory.getBaseServiceDao();
    }

    @Override
    public BaseService findById(Integer id) throws ServiceException {
        try {
            return bsDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findAll(String locale) throws ServiceException {
        try {
            return toDtos(bsDao.findAll(), locale);
        } catch (DaoException e) {
            LOG.error("Error to get all base services");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(BaseService baseService) throws ServiceException {
        try {
            if (baseService.isNew()) {
                bsDao.create(baseService);
            } else bsDao.update(baseService);
        } catch (DaoException e) {
            LOG.error("Error to save base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer baseServiceId) throws ServiceException {
        try {
            bsDao.removeById(baseServiceId);
        } catch (DaoException e) {
            LOG.error("Error to delete base service by id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findByFilter(List<Integer> categoriesIds, int page, int size, String locale) throws ServiceException {
        try {
            return toDtos(bsDao.findByFilter(categoriesIds, page, size), locale);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    @Override
    public int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws ServiceException {
        try {
            return bsDao.getCountOfCategories(categoriesIds, page, size);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private List<BaseServiceDto> toDtos(List<BaseService> baseServices, String locale) throws DaoException {
        List<BaseServiceDto> dtos = new ArrayList<>();
        for (BaseService baseService : baseServices) {
            dtos.add(toDto(baseService, locale));
        }
        return dtos;
    }

    private BaseServiceDto toDto(BaseService baseService, String locale) throws DaoException {
        BaseServiceDto dto = new BaseServiceDto();
        ServiceCategory category = scs.findById(baseService.getCategoryId());
        dto.setId(baseService.getId());
        if (Objects.equals(locale, "ua")) {
            dto.setService(baseService.getServiceUa());
            dto.setCategory(category.getNameUa());
        } else {
            dto.setService(baseService.getService());
            dto.setCategory(category.getName());
        }
        dto.setPrice(baseService.getPrice());
        return dto;
    }
}
