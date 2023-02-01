package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.Invitation;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

public interface InvitationService {

    Invitation findByEmailAndKey(String email, String key) throws ServiceException;

    void create(String email, Role role) throws ServiceException;
}
