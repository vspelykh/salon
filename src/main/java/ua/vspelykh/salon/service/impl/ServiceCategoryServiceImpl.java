package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.dao.ServiceCategoryDao;
import ua.vspelykh.salon.dto.ServiceCategoryDto;
import ua.vspelykh.salon.model.ServiceCategory;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.mapper.Column.UA_LOCALE;

public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private ServiceCategoryDao serviceCategoryDao;
    private Transaction transaction;

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
//            e.printStackTrace();
            //TODO
            throw new ServiceException(e);
        }
    }

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
