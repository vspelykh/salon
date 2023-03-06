package ua.vspelykh.salon.service.impl;

import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.model.dao.InvitationDao;
import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.service.InvitationService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.service.email.EmailService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import static ua.vspelykh.salon.util.SalonUtils.generateKeyString;

/**
 * This class implements the InvitationService interface to create invitations with a unique key and email
 * that will be used to register a new user with a specific role in the system. The invitation can be removed
 * if it already exists with the same email.
 * <p>
 * The InvitationServiceImpl class also encrypts the key and sends an email with the invitation key
 * to the email address provided.
 *
 * @version 1.0
 */
public class InvitationServiceImpl implements InvitationService {

    private InvitationDao invitationDao;
    private Transaction transaction;

    /**
     * Creates a new invitation with a unique key and email that will be used to register
     * a new user with a specific role in the system.
     *
     * @param email the email address of the user to be invited
     * @param role  the role of the user to be invited
     * @throws ServiceException if there is an error creating the invitation
     */
    @Override
    public void create(String email, Role role) throws ServiceException {
        try {
            transaction.start();
            invitationDao.removeByEmailIfExists(email);
            String key = generateKeyString();
            invitationDao.create(new Invitation(email, role, encryptKey(key)));
            EmailService.sendEmail(email, "Invitation to reg", "Your key: '" + key + "'");
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    /**
     * Encrypts the provided key using a BasicPasswordEncryptor.
     *
     * @param key the key to be encrypted
     * @return the encrypted key
     */
    private String encryptKey(String key) {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        return encryptor.encryptPassword(key);
    }

    public void setInvitationDao(InvitationDao invitationDao) {
        this.invitationDao = invitationDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
