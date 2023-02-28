package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface ConsultationService {

    List<Consultation> findAll() throws ServiceException;

    void save(Consultation consultation) throws ServiceException;

    void delete(Integer id) throws ServiceException;

}
