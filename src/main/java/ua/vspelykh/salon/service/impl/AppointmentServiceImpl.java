package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.dto.UserDto;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AppointmentService interface.
 * Handles all the business logic related to appointments.
 *
 * @version 1.0
 */
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LogManager.getLogger(AppointmentServiceImpl.class);

    private AppointmentDao appointmentDao;
    private UserDao userDao;
    private AppointmentItemDao appointmentItemDao;
    private Transaction transaction;

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve
     * @return the appointment with the specified ID
     * @throws ServiceException if there was an error retrieving the appointment
     */
    @Override
    public Appointment findById(Integer id) throws ServiceException {
        try {
            return appointmentDao.findById(id);
        } catch (DaoException e) {
            LOG.error("Error to find service");
            throw new ServiceException(e);
        }
    }

    /**
     * Saves an appointment to the database.
     *
     * @param appointment the appointment to save
     * @throws ServiceException if there was an error saving the appointment
     */
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

    /**
     * Saves an appointment and a list of master services to the database.
     *
     * @param appointment    the appointment to save
     * @param masterServices the list of master services to save
     * @throws ServiceException if there was an error saving the appointment or master services
     */
    @Override
    public void save(Appointment appointment, List<MasterService> masterServices) throws ServiceException {
        try {
            transaction.start();
            if (appointment.isNew()) {
                int id = appointmentDao.create(appointment);
                appointment.setId(id);
            } else {
                appointmentDao.update(appointment);
            }
            for (MasterService masterService : masterServices) {
                appointmentItemDao.create(new AppointmentItem(appointment.getId(), masterService.getId()));
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

    /**
     * Deletes an appointment from the database.
     *
     * @param id the ID of the appointment to delete
     * @throws ServiceException if there was an error deleting the appointment
     */
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

    /**
     * Retrieves a list of appointments for a specific date and master ID.
     *
     * @param date     the date to filter appointments by
     * @param masterId the ID of the master to filter appointments by
     * @return a list of appointments for the specified date and master ID
     * @throws ServiceException if there was an error retrieving the appointments
     */
    @Override
    public List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws ServiceException {
        try {
            return appointmentDao.getByDateAndMasterId(date, masterId);
        } catch (DaoException e) {
            LOG.error("Error to find appointment by date and master id");
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves a list of appointment DTOs for a specific date and master ID.
     *
     * @param date     the date to filter appointments by
     * @param masterId the ID of the master to filter appointments by
     * @return a list of appointment DTOs for the specified date and master ID
     * @throws ServiceException if there was an error retrieving the appointment DTOs
     */
    @Override
    public List<AppointmentDto> getDTOsByDateAndMasterId(LocalDate date, int masterId) throws ServiceException {
        try {
            transaction.start();
            List<AppointmentDto> appointmentDTOs = toDTOs(getByDateAndMasterId(date, masterId));
            transaction.commit();
            return appointmentDTOs;
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

    /**
     * Retrieves a list of appointment DTOs for a specific date.
     *
     * @param date the date to filter appointments by
     * @return a list of appointment DTOs for the specified date
     * @throws ServiceException if there was an error retrieving the appointment DTOs
     */
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

    /**
     * Retrieves a filtered list of appointments from the database based on the specified filter, page, and size.
     *
     * @param filter the filter to apply to the appointments
     * @param page   the page number of the results to retrieve
     * @param size   the maximum number of results to retrieve per page
     * @return a list of AppointmentDto objects representing the filtered appointments
     * @throws ServiceException if an error occurs while retrieving the appointments
     */
    @Override
    public List<AppointmentDto> getFiltered(AppointmentFilter filter, int page, int size) throws ServiceException {
        try {
            transaction.start();
            List<AppointmentDto> appointmentDTOs = toDTOs(appointmentDao.getFiltered(filter, page, size));
            transaction.commit();
            return appointmentDTOs;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to find appointments by filter params");
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves the count of appointments in the database based on the specified filter.
     *
     * @param filter the filter to apply to the appointments
     * @return the count of appointments that match the filter
     * @throws ServiceException if an error occurs while retrieving the count
     */
    @Override
    public int getCountOfAppointments(AppointmentFilter filter) throws ServiceException {
        try {
            return appointmentDao.getCountOfAppointments(filter);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Converts a list of Appointment objects to a list of AppointmentDto objects.
     *
     * @param appointments the list of Appointment objects to convert
     * @return a list of AppointmentDto objects representing the appointments
     * @throws DaoException if an error occurs while converting the appointments
     */
    private List<AppointmentDto> toDTOs(List<Appointment> appointments) throws DaoException {
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            dtos.add(toDTO(appointment));
        }
        return dtos;
    }

    /**
     * Converts an Appointment object to an AppointmentDto object.
     *
     * @param appointment the Appointment object to convert
     * @return an AppointmentDto object representing the appointment
     * @throws DaoException if an error occurs while converting the appointment
     */
    private AppointmentDto toDTO(Appointment appointment) throws DaoException {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .master(userToDTO(userDao.findById(appointment.getMasterId())))
                .client(userToDTO(userDao.findById(appointment.getClientId())))
                .continuance(appointment.getContinuance())
                .date(appointment.getDate())
                .price(appointment.getPrice())
                .discount(appointment.getDiscount())
                .appointmentItems(appointmentItemDao.getByAppointmentId(appointment.getId()))
                .status(appointment.getStatus())
                .paymentStatus(appointment.getPaymentStatus())
                .build();
    }

    /**
     * Converts a User object to a UserDto object.
     *
     * @param user the User object to convert
     * @return a UserDto object representing the user
     */
    private UserDto userToDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .number(user.getNumber())
                .roles(user.getRoles())
                .build();
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setAppointmentItemDao(AppointmentItemDao appointmentItemDao) {
        this.appointmentItemDao = appointmentItemDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
