package ua.vspelykh.salon.util.exception;

/**
 * A utility class containing message constants used across the application.
 *
 * @version 1.0
 */
public final class Messages {

    public static final String MESSAGE_FIELDS_EMPTY = "fields";
    public static final String MESSAGE_PASSWORDS_MISMATCH = "passwords";
    public static final String MESSAGE_REGISTRATION_EMAIL_EXISTS = "registration.email";
    public static final String MESSAGE_REGISTRATION_NUMBER_EXISTS = "registration.number_exists";
    public static final String MESSAGE_BAD_PASSWORD = "registration.password";
    public static final String MESSAGE_KEY = "registration.key";
    public static final String MESSAGE_REGISTRATION_OTHER_ERROR = "registration.other";
    public static final String MESSAGE_DELETE_DAYS = "error.days";
    public static final String MESSAGE_REGISTRATION_SUCCESS = "registration.success";
    public static final String MESSAGE_APPOINTMENT_SUCCESS = "appointment.success";
    public static final String MESSAGE_POSTPONEMENT_SUCCESS = "postponement.success";
    public static final String MESSAGE_POSTPONEMENT_FAIL = "postponement.fail";
    public static final String MESSAGE_APPOINTMENT_FAIL = "The time slot is already occupied or the duration is no longer allowed.";
    public static final String MESSAGE_FEEDBACK_EXISTS = "message.feedback.exist";
    public static final String MESSAGE_FEEDBACK = "message.mark";
    public static final String MESSAGE_SUCCESS_INVITATION = "success.invitation";
    public static final String CONTROLLER_ERROR = "Error in FrontController";
    public static final String ERROR_PARSING_LOCAL_DATE = "Error parsing LocalDate for schedule";
    public static final String ERROR_FORMATTING_LDT = "Error formatting LocalDateTime";
    public static final String ERROR_WRITING_TO_JSP = "Error writing to JspWriter";
    public static final String ERROR_PRINT_STARS = "Error to print stars for feedback";
    public static final String ERROR_500 = "500.error";
    public static final String ERROR_404 = "404.exist";
    public static final String ERROR_SCHEDULE_BUILDER = "Error getting service names during schedule building";
    public static final String ERROR_CREATE_APPOINTMENT_ON_WEEKEND = "Trying to create an appointment on weekend day";
    public static final String CONSULTATION_ERROR = "consultation.error";
    public static final String CONSULTATION_SUCCESS = "consultation.success";
    public static final String EDIT_APPOINTMENT_ERROR = "edit.app.error";
    public static final String POST_FEEDBACK_ERROR = "post.feedback.error";
    public static final String CONSULTATION_EDIT_ERROR = "cons.edit.error";
    public static final String EDIT_SCHEDULE_ERROR = "edit.schedule.error";
    public static final String EDIT_ROLE_ERROR = "edit.role.error";
    public static final String EDIT_MASTER_ERROR = "edit.master.error";

    private Messages() {
    }
}
