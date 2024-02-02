package ua.vspelykh.feedbackmicroservice.service.interfaces;

/**
 * The EmailService interface defines methods for sending emails, including
 * general messages and password reset links.
 */
public interface EmailService {

    /**
     * Sends a general message to the specified email address.
     *
     * @param email   The email address of the recipient.
     * @param message The message content to be sent.
     * @return true if the message was successfully sent, and false otherwise.
     */
    boolean sendMessageToEmail(String email, String message);
}

