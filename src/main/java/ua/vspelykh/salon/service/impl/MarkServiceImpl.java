package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.MarkDao;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dto.MarkDto;
import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.MarkService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class MarkServiceImpl implements MarkService {

    private static final Logger LOG = LogManager.getLogger(MarkServiceImpl.class);

    private MarkDao markDao;
    private AppointmentDao appointmentDao;
    private UserDao userDao;

    @Override
    public void save(Mark mark) throws ServiceException {
        try {
            markDao.create(mark);
        } catch (DaoException e) {
            LOG.error("Error to save mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MarkDto> getMarksByMasterId(Integer masterId, int page) throws ServiceException {
        try {
            List<Mark> marks = markDao.getMarksByMasterId(masterId, page);
            return toDTOs(marks);
        } catch (DaoException e) {
            LOG.error("Error to get marks for master");
            throw new ServiceException(e);
        }
    }

    private List<MarkDto> toDTOs(List<Mark> marks) throws DaoException {
        List<MarkDto> dtos = new ArrayList<>();
        for (Mark mark : marks){
            dtos.add(toDTO(mark));
        }
        return dtos;
    }

    private MarkDto toDTO(Mark mark) throws DaoException {
        User client = userDao.findById(appointmentDao.findById(mark.getAppointmentId()).getClientId());
        return new MarkDto.MarkDtoBuilder(client, mark).build();
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            markDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public int countMarksByMasterId(Integer masterID) throws ServiceException {
        try {
            return markDao.countMarksByMasterId(masterID);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public void setMarkDao(MarkDao markDao) {
        this.markDao = markDao;
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}