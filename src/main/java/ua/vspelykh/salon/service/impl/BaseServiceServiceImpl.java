package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dto.BaseServiceDto;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

public class BaseServiceServiceImpl implements BaseServiceService {

    private static final Logger LOG = LogManager.getLogger(BaseServiceServiceImpl.class);

    private ServiceCategoryService serviceCategoryService;
    private BaseServiceDao baseServiceDao;
    private Transaction transaction;

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
            transaction.start();
            List<BaseServiceDto> baseServiceDtos = toDtos(baseServiceDao.findAll(), locale);
            transaction.commit();
            return baseServiceDtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to get all base services");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(BaseService baseService) throws ServiceException {
        try {
            transaction.start();
            if (baseService.isNew()) {
                baseServiceDao.create(baseService);
            } else {
                baseServiceDao.update(baseService);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer baseServiceId) throws ServiceException {
        try {
            transaction.start();
            baseServiceDao.removeById(baseServiceId);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to delete base service by id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findByFilter(List<Integer> categoriesIds, int page, int size, String locale) throws ServiceException {
        try {
            transaction.start();
            List<BaseServiceDto> baseServiceDtos = toDtos(baseServiceDao.findByFilter(categoriesIds, page, size), locale);
            transaction.commit();
            return baseServiceDtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws ServiceException {
        try {
            return baseServiceDao.getCountOfCategories(categoriesIds);
        } catch (DaoException e) {
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

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
