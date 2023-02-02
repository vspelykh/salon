package ua.vspelykh.salon.service.impl;

import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.dao.InvitationDao;
import ua.vspelykh.salon.model.Invitation;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.service.EmailService;
import ua.vspelykh.salon.service.InvitationService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;

import static ua.vspelykh.salon.util.SalonUtils.generateKeyString;

public class InvitationServiceImpl implements InvitationService {

    private InvitationDao invitationDao;

    @Override
    public Invitation findByEmailAndKey(String email, String key) throws ServiceException {
        try {
            Invitation invitation = invitationDao.findByEmail(email);
            BasicPasswordEncryptor keyEncryptor = new BasicPasswordEncryptor();
            if (keyEncryptor.checkPassword(key, invitation.getKey())) {
                return invitation;
            } else if (invitation.getDate().plusDays(7).isBefore(LocalDate.now())) {
                throw new ServiceException("Too late, need to get new invitation");
            } else throw new ServiceException("Incorrect key or invitation doesn't exist");

        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(String email, Role role) throws ServiceException {
        try {
            invitationDao.removeByEmailIfExists(email);
            String key = generateKeyString();
            invitationDao.create(new Invitation(email, role, encryptKey(key)));
            EmailService.sendEmail(email, "Invitation to reg", "Your key: '" + key + "'");
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private String encryptKey(String key) {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        return encryptor.encryptPassword(key);
    }

    public void setInvitationDao(InvitationDao invitationDao) {
        this.invitationDao = invitationDao;
    }
}
