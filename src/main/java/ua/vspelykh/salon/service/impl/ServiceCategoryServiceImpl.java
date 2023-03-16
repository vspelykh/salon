package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dto.ServiceCategoryDto;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

/**
 * Implements ServiceCategoryService interface to provide functionality to access service category data.
 *
 * @version 1.0
 */
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private ServiceCategoryDao serviceCategoryDao;
    private Transaction transaction;

    /**
     * Retrieves a service category by its ID.
     *
     * @param id ID of the service category to retrieve.
     * @return ServiceCategory object representing the service category.
     * @throws ServiceException if there was an error while accessing the service category data.
     */
    @Override
    public ServiceCategory findById(int id) throws ServiceException {
        try {
            return serviceCategoryDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves a list of ServiceCategoryDto objects representing all service categories.
     *
     * @param locale the locale used to determine the language of the service category names in the
     *               ServiceCategoryDto objects.
     * @return List of ServiceCategoryDto objects representing all service categories.
     * @throws ServiceException if there was an error while accessing the service category data or managing transactions.
     */
    @Override
    public List<ServiceCategoryDto> findAll(String locale) throws ServiceException {
        try {
            transaction.start();
            List<ServiceCategoryDto> dtos = new ArrayList<>();
            for (ServiceCategory category : serviceCategoryDao.findAll()) {
                dtos.add(toDto(category, locale));
            }
            transaction.commit();
            return dtos;
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
     * Converts a ServiceCategory object to a ServiceCategoryDto object.
     *
     * @param category ServiceCategory object to convert.
     * @param locale   the locale used to determine the language of the service category name in the
     *                 ServiceCategoryDto object.
     * @return ServiceCategoryDto object representing the ServiceCategory object.
     */
    private ServiceCategoryDto toDto(ServiceCategory category, String locale) {
        return new ServiceCategoryDto(category.getId(), UA_LOCALE.equals(locale) ? category.getNameUa() : category.getName());
    }

    public void setServiceCategoryDao(ServiceCategoryDao serviceCategoryDao) {
        this.serviceCategoryDao = serviceCategoryDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
