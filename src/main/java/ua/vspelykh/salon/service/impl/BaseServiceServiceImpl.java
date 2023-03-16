package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dto.BaseServiceDto;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

/**
 * This class implements the BaseServiceService interface and provides implementation for its methods.
 *
 * @version 1.0
 */
public class BaseServiceServiceImpl implements BaseServiceService {

    private static final Logger LOG = LogManager.getLogger(BaseServiceServiceImpl.class);

    private ServiceCategoryDao serviceCategoryDao;
    private BaseServiceDao baseServiceDao;
    private Transaction transaction;

    /**
     * Retrieves a BaseService object with the given id.
     *
     * @param id the id of the BaseService to retrieve.
     * @return a BaseService object with the given id.
     * @throws ServiceException if an error occurs while retrieving the BaseService.
     */
    @Override
    public BaseService findById(Integer id) throws ServiceException {
        try {
            return baseServiceDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves all BaseService objects as BaseServiceDto objects in the specified locale.
     *
     * @param locale the locale in which to retrieve the BaseServiceDto objects.
     * @return a List of BaseServiceDto objects in the specified locale.
     * @throws ServiceException if an error occurs while retrieving the BaseServiceDto objects.
     */
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

    /**
     * Saves the specified BaseService object.
     * If the BaseService is new, it will be created. If it already exists, it will be updated.
     *
     * @param baseService the BaseService object to save.
     * @throws ServiceException if an error occurs while saving the BaseService object.
     */
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

    /**
     * Deletes the BaseService object with the specified id.
     *
     * @param baseServiceId the id of the BaseService to delete.
     * @throws ServiceException if an error occurs while deleting the BaseService object.
     */
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

    /**
     * Retrieves a List of BaseServiceDto objects that match the specified filter parameters.
     *
     * @param categoriesIds a List of category ids to filter by.
     * @param page          the page number of the results.
     * @param size          the number of results per page.
     * @param locale        the locale in which to retrieve the BaseServiceDto objects.
     * @return a List of BaseServiceDto objects that match the specified filter parameters.
     * @throws ServiceException if an error occurs while retrieving the BaseServiceDto objects.
     */
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

    /**
     * Retrieves the count of BaseService objects that match the specified filter parameters.
     *
     * @param categoriesIds a List of category ids to filter by.
     * @param page          the page number of the results.
     * @param size          the number of results per page.
     * @return the count of BaseService objects that match the specified filter parameters.
     * @throws ServiceException if an error occurs while retrieving the count.
     */
    @Override
    public int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws ServiceException {
        try {
            return baseServiceDao.getCountOfCategories(categoriesIds);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Converts a List of BaseService objects to a List of BaseServiceDto objects in the specified locale.
     *
     * @param baseServices the List of BaseService objects to convert.
     * @param locale       the locale in which to convert the objects.
     * @return a List of BaseServiceDto objects in the specified locale.
     * @throws DaoException if an error occurs while converting the objects.
     */
    private List<BaseServiceDto> toDtos(List<BaseService> baseServices, String locale) throws DaoException {
        List<BaseServiceDto> dtos = new ArrayList<>();
        for (BaseService baseService : baseServices) {
            dtos.add(toDto(baseService, locale));
        }
        return dtos;
    }

    /**
     * Converts an BaseService object to an BaseServiceDto object.
     *
     * @param baseService the Appointment object to convert
     * @return an BaseServiceDto object representing the baseService
     * @throws DaoException if an error occurs while converting the baseService
     */
    private BaseServiceDto toDto(BaseService baseService, String locale) throws DaoException {
        BaseServiceDto dto = new BaseServiceDto();
        ServiceCategory category = serviceCategoryDao.findById(baseService.getCategoryId());
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

    public void setServiceCategoryDao(ServiceCategoryDao serviceCategoryDao) {
        this.serviceCategoryDao = serviceCategoryDao;
    }

    public void setBaseServiceDao(BaseServiceDao baseServiceDao) {
        this.baseServiceDao = baseServiceDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
