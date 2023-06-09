package ua.vspelykh.salon.controller.command;

/**
 * The CommandNames class provides a set of constant strings that represent the commands used in the application.
 * Each command string represents a unique action that can be performed on the application, such as logging in or
 * creating an appointment. The strings are used in conjunction with the CommandFactory to create the appropriate
 * Command object for a given HTTP request.
 *
 * @version 1.0
 */
public class CommandNames {

    public static final String ABOUT = "about";
    public static final String HOME = "home";
    public static final String PRICING = "pricing";
    public static final String MASTERS = "masters";
    public static final String LOGIN = "login";
    public static final String CHECK_LOGIN = "checkLogin";
    public static final String LOGOUT = "logout";
    public static final String SIGN_UP_FORM = "sign-up";
    public static final String REGISTRATION = "reg";
    public static final String SUCCESS = "success";
    public static final String PROFILE = "profile";
    public static final String ADMIN = "admin";
    public static final String CHANGE_ROLE = "change-role";
    public static final String CONSULTATION = "consultation";
    public static final String CONSULTATION_POST = "consultation-post";
    public static final String CONSULTATION_GET = "consultations";
    public static final String CONSULTATION_EDIT = "consultation-edit";
    public static final String CALENDAR = "calendar";
    public static final String SCHEDULE = "schedule";
    public static final String GET_SCHEDULE = "get-schedule";
    public static final String LOOK_SCHEDULE = "look-schedule";
    public static final String EDIT_SCHEDULE = "edit-schedule";
    public static final String APPOINTMENT = "appointment";
    public static final String APPOINTMENTS = "appointments";
    public static final String CREATE_APPOINTMENT = "create-appointment";
    public static final String FEEDBACK = "mark";
    public static final String FEEDBACK_POST = "mark-post";
    public static final String EDIT_APPOINTMENT = "edit-appointment";
    public static final String ORDERS = "orders";
    public static final String CREATE_INVITATION = "create-invitation";
    public static final String INVITATION = "invitation";
    public static final String EDIT_MASTER = "edit-master";
    public static final String ERROR_COMMAND = "error";
    public static final String POSTPONE_FORM = "postpone-form";
    public static final String POSTPONEMENT = "postponement";

    /**
     * A private constructor to prevent the instantiation of this class from external classes.
     */
    private CommandNames() {
    }
}
