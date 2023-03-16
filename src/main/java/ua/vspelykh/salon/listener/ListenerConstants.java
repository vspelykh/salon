package ua.vspelykh.salon.listener;

/**
 * The ListenerConstants class provides constant values that are commonly used by listener classes.
 * <p>
 * This class is a utility class and cannot be instantiated.
 *
 * @version 1.0
 */
public class ListenerConstants {

    private ListenerConstants() {

    }

    public static final String KYIV = "Europe/Kiev";
    public static final String FEEDBACK_THEME = "Feedback about the visit";
    public static final String FEEDBACK_EMAIL = "Dear %s.<br>  Please leave mark about your visit to the %s yesterday."
            + "Hope you enjoyed your visit to our salon!<br>  We’re constantly striving to provide excellent service for " +
            "our clients. We’d love to get your mark through a brief customer survey.<br>" +
            "<a href=%s>Leave mark on our website.</a> <br> Thank you for your time. We really appreciate it!";

    public static final String ERROR_CLOSE_CONNECTION = "Error to close connection in Listener.";
    public static final String ERROR_NOTIFICATION = "Error to get consultations for notification.";
    public static final String ERROR_ATTR = "Error to update session attr.";

}