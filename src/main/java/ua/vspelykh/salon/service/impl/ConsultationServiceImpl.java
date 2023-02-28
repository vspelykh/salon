package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dao.ConsultationDao;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

public class ConsultationServiceImpl implements ConsultationService {

    private ConsultationDao dao;
    private Transaction transaction;

    @Override
    public List<Consultation> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Consultation consultation) throws ServiceException {
        try {
            transaction.start();
            dao.create(consultation);
            transaction.commit();
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
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            dao.removeById(id);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    public void setDao(ConsultationDao dao) {
        this.dao = dao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
