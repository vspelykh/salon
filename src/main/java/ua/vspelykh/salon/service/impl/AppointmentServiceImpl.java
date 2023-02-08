package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.OrderingDao;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.dto.UserDto;
import ua.vspelykh.salon.model.*;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LogManager.getLogger(AppointmentServiceImpl.class);

    private AppointmentDao appointmentDao;
    private UserDao userDao;
    private OrderingDao orderingDao;
    private Transaction transaction;

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
            transaction.start();
            if (appointment.isNew()) {
                appointmentDao.create(appointment);
            } else {
                appointmentDao.update(appointment);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save an appointment");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Appointment appointment, List<Service> services) throws ServiceException {
        try {
            transaction.start();
            if (appointment.isNew()) {
                int id = appointmentDao.create(appointment);
                appointment.setId(id);
            } else {
                appointmentDao.update(appointment);
            }
            for (Service service : services) {
                orderingDao.create(new Ordering(appointment.getId(), service.getId()));
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save an appointment");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            appointmentDao.removeById(id);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
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
            transaction.start();
            List<AppointmentDto> appointmentDtos = toDTOs(getByDateAndMasterId(date, masterId));
            transaction.commit();
            return appointmentDtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to find appointment by date and master id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AppointmentDto> getAllByDate(LocalDate date) throws ServiceException {
        try {
            List<Appointment> appointments = appointmentDao.getAllByDate(date);
            return toDTOs(appointments);
        } catch (DaoException e) {
            LOG.error("Error to find appointment by date");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AppointmentDto> getFiltered(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus, int page, int size) throws ServiceException {
        try {
            transaction.start();
            List<AppointmentDto> appointmentDtos = toDTOs(appointmentDao.getFiltered(masterId, dateFrom, dateTo,
                    status, paymentStatus, page, size));
            transaction.commit();
            return appointmentDtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public int getCountOfAppointments(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) throws ServiceException {
        try {
            return appointmentDao.getCountOfAppointments(masterId, dateFrom, dateTo, status, paymentStatus);
        } catch (DaoException e) {
            e.printStackTrace();
            //TODO
            throw new ServiceException(e);
        }
    }

    private List<AppointmentDto> toDTOs(List<Appointment> appointments) throws DaoException {
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            dtos.add(toDTO(appointment));
        }
        return dtos;
    }

    private AppointmentDto toDTO(Appointment appointment) throws DaoException {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setMaster(userToDto(userDao.findById(appointment.getMasterId())));
        dto.setClient(userToDto(userDao.findById(appointment.getClientId())));
        dto.setContinuance(appointment.getContinuance());
        dto.setDate(appointment.getDate());
        dto.setPrice(appointment.getPrice());
        dto.setDiscount(appointment.getDiscount());
        dto.setOrderings(orderingDao.getByAppointmentId(appointment.getId()));
        dto.setStatus(appointment.getStatus());
        dto.setPaymentStatus(appointment.getPaymentStatus());
        return dto;
    }

    private UserDto userToDto(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getNumber());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setOrderingDao(OrderingDao orderingDao) {
        this.orderingDao = orderingDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
