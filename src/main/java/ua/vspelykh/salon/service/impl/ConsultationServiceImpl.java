package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.dao.ConsultationDao;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.model.Consultation;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public class ConsultationServiceImpl implements ConsultationService {

    private ConsultationDao dao = DaoFactory.getConsultationDao();

    @Override
    public List<Consultation> findAll() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Consultation consultation) throws ServiceException {
        try {
            dao.create(consultation);
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            dao.removeById(id);
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }
}
