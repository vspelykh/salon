package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

/**
 * This interface defines the methods to manage invitations.
 *
 * @version 1.0
 */
public interface InvitationService {

    /**
     * Creates a new invitation with the specified email and role.
     *
     * @param email the email address of the user to whom the invitation is being sent
     * @param role  the role to be assigned to the user when they accept the invitation
     * @throws ServiceException if an error occurs while creating the invitation
     */
    void create(String email, Role role) throws ServiceException;
}