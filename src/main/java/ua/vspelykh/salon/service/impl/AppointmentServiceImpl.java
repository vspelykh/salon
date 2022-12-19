package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LogManager.getLogger(AppointmentServiceImpl.class);

    private final AppointmentDao appointmentDao = DaoFactory.getAppointmentDao();

    @Override
    public Appointment findById(Integer id) throws ServiceException {
        try {
            return appointmentDao.findById(id);
        } catch (DaoException e) {
            LOG.error("Error to find service");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Appointment appointment) throws ServiceException {
        try {
            if (appointment.isNew()) {
                appointmentDao.create(appointment);
            } else appointmentDao.update(appointment);
        } catch (DaoException e){
            LOG.error("Error to save an appointment");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            appointmentDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete an appointment");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Appointment> getByMasterId(Integer masterId) throws ServiceException {
        try {
            return appointmentDao.getByMasterId(masterId);
        } catch (DaoException e) {
            LOG.error("Error to find appointments by master id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Appointment> getByClientId(Integer clientId) throws ServiceException {
        try {
            return appointmentDao.getByClientId(clientId);
        } catch (DaoException e) {
            LOG.error("Error to find appointment by client id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws ServiceException {
        try {
            return appointmentDao.getByDateAndMasterId(date, masterId);
        } catch (DaoException e) {
            LOG.error("Error to find appointment by date and master id");
            throw new ServiceException(e);
        }
    }
}
