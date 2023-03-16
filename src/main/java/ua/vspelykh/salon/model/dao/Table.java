package ua.vspelykh.salon.model.dao;

/**
 * This class provides a list of table names used in the database.
 *
 * @version 1.0
 */
public final class Table {

    public static final String USER = "users";
    public static final String USER_LEVEL = "user_level";
    public static final String USER_ROLES = "user_roles";
    public static final String SERVICE_CATEGORY = "service_categories";
    public static final String BASE_SERVICE = "base_services";
    public static final String MASTER_SERVICES = "master_services";
    public static final String APPOINTMENT = "appointments";
    public static final String APPOINTMENT_ITEMS = "appointment_items";
    public static final String FEEDBACKS = "feedbacks";
    public static final String CONSULTATION = "consultations";
    public static final String WORKING_DAY = "working_days";
    public static final String INVITATION = "invitations";

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private Table() {
    }
}
