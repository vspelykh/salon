package ua.vspelykh.salon.model.entity;

/**
 * The Role enum represents the roles that users can have in the application.
 * <p>
 * This enum includes the following roles:
 * GUEST: a non-registered user who has limited access to the application.
 * ADMINISTRATOR: a user who has administrative privileges and can perform management tasks.
 * CLIENT: a user who has registered and can book appointments and leave feedback.
 * HAIRDRESSER: a user who has registered as a hairdresser and can view and manage their appointments.
 *
 * @version 1.0
 */
public enum Role {
    GUEST, ADMINISTRATOR, CLIENT, HAIRDRESSER
}
