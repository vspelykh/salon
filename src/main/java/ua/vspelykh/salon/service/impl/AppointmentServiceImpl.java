package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.OrderingDao;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.util.validation.Validation.validateAppointment;

public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LogManager.getLogger(AppointmentServiceImpl.class);

    private final AppointmentDao appointmentDao = DaoFactory.getAppointmentDao();
    private final UserDao userDao = DaoFactory.getUserDao();
    private final OrderingDao orderingDao = DaoFactory.getOrderingDao();

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
        if (!validateAppointment(appointment)){
            throw new ServiceException("Time slot have already occupied or duration not allowed anymore.");
        }
        try {
            if (appointment.isNew()) {
                appointmentDao.create(appointment);
            } else appointmentDao.update(appointment);
        } catch (DaoException e) {
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

    @Override
    public List<AppointmentDto> getDtosByDateAndMasterId(LocalDate date, int masterId) throws ServiceException {
        try {
            return toDTOs(getByDateAndMasterId(date, masterId));
        } catch (DaoException e) {
            LOG.error("Error to find appointment by date and master id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AppointmentDto> getAllByDate(LocalDate date) throws ServiceException {
        try {
            List<Appointment> appointments = appointmentDao.getAllByDate(date);
            return toDTOs(appointments);
        } catch (DaoException e){
            LOG.error("Error to find appointment by date");
            throw new ServiceException(e);
        }
    }

    private List<AppointmentDto> toDTOs(List<Appointment> appointments) throws DaoException {
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment appointment : appointments){
            dtos.add(toDTO(appointment));
        }
        return dtos;
    }

    private AppointmentDto toDTO(Appointment appointment) throws DaoException {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setMaster(userDao.findById(appointment.getMasterId()));
        dto.setClient(userDao.findById(appointment.getClientId()));
        dto.setContinuance(appointment.getContinuance());
        dto.setDate(appointment.getDate());
        dto.setPrice(appointment.getPrice());
        dto.setDiscount(appointment.getDiscount());
        dto.setOrderings(orderingDao.getByAppointmentId(appointment.getId()));
        return dto;
    }
}
