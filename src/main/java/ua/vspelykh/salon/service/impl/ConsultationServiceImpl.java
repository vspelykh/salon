package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dao.ConsultationDao;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

/**
 * Implementation of the ConsultationService interface.
 *
 * @version 1.0
 */
public class ConsultationServiceImpl implements ConsultationService {

    private ConsultationDao dao;
    private Transaction transaction;

    /**
     * Returns all consultations.
     *
     * @return a list of all consultations.
     * @throws ServiceException if there is an error retrieving the consultations.
     */
    @Override
    public List<Consultation> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Saves a consultation.
     *
     * @param consultation the consultation to be saved.
     * @throws ServiceException if there is an error creating the consultation.
     */
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

    /**
     * Deletes a consultation with the specified ID.
     *
     * @param id the ID of the consultation to be deleted.
     * @throws ServiceException if there is an error deleting the consultation.
     */
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
