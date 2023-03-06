package ua.vspelykh.salon.controller;

import java.util.List;

/**
 * This class holds all the constant values used in the controller.
 */
public class ControllerConstants {

    // Command parameter names
    public static final String COMMAND = "command";
    public static final String REDIRECT = "redirect";
    public static final String REMOVE = "remove";
    public static final String ADD = "add";

    // Command values
    public static final String SIGN_UP = "sign-up";
    public static final String SUCCESS = "success";
    public static final String LOGIN = "login";
    public static final String HOME_PAGE = "index";
    public static final String PROFILE = "profile";
    public static final String MASTERS = "masters";
    public static final String FEEDBACKS = "feedbacks";

    // Redirect paths
    public static final String SUCCESS_REDIRECT = "/salon?command=success";
    public static final String CONSULTATION_REDIRECT = "/salon?command=consultations";
    public static final String LOGIN_REDIRECT = "/salon?command=login";
    public static final String APPOINTMENT_REDIRECT_PATTERN = "%s%s%s&%s=%s&%s=%s";
    public static final String CALENDAR_REDIRECT = "?command=calendar&day=";
    public static final String ID_PARAM_REDIRECT = "&id=";
    public static final String MASTERS_REDIRECT = "/salon?command=masters";
    public static final String ROLES_REDIRECT = "/salon?command=roles&search=";
    public static final String SCHEDULE_REDIRECT = "/salon?command=schedule&id=";
    public static final String HOME_REDIRECT = "/salon";
    public static final String COMMAND_PARAM = "?command=";

    // Request parameter names
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String NEW_SLOT = "new_slot";
    public static final String INS_LOGIN = "insLogin";
    public static final String INS_PASSWORD = "insPassword";
    public static final String MESSAGE = "message";
    public static final String USER = "user";
    public static final String USERS = "users";
    public static final String LEVELS = "levels";
    public static final String SERVICES = "services";
    public static final String CATEGORIES = "categories";
    public static final String SIZE = "size";
    public static final String SEARCH = "search";
    public static final String SORT = "sort";
    public static final String PAGE = "page";
    public static final String CHECKED = "Checked";
    public static final String LAST_PAGE = "lastPage";
    public static final String PAGES_ARRAY = "pagesArray";
    public static final String NUMBER_OF_PAGES = "numOfPages";
    public static final String PATH_STR = "pathStr";
    public static final String ACTION = "action";
    public static final String DAYS = "days";
    public static final String FIRST = "first";
    public static final String PAYMENT = "payment";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email";
    public static final String NUMBER = "number";
    public static final String PASSWORD_REPEAT = "passwordRepeat";
    public static final String PAGE_PARAM_REGEX = "&page=[0-9]*";
    public static final String FREE_SLOTS_MAP = "free_slots_map";
    public static final String PASSWORD = "password";
    public static final String CURRENT_USER = "currentUser";
    public static final List<Integer> SIZE_LIST = List.of(1, 2, 5, 10);
    public static final String SORTS = "sorts";
    public static final String ALLOWED_TIME = "allowedTime";
    public static final int DEFAULT_SIZE = 5;
    public static final int DEFAULT_PAGE = 1;
    public static final double DEFAULT_DISCOUNT = 1.0;
    public static final String PLACEHOLDER_EN = "Pick A Date";
    public static final String PLACEHOLDER_UA = "Виберіть дату";
    public static final String DAY = "day";
    public static final String TIME = "time";
    public static final String SLOTS = "slots";
    public static final String PLACEHOLDER = "placeholder";
    public static final int INTERVAL = 30;

    // User roles
    public static final String ROLES = "roles";
    public static final String IS_MASTER = "isMaster";
    public static final String IS_ADMIN = "isAdmin";
    public static final String IS_CLIENT = "isClient";
    public static final String USER_LEVEL = "userLevel";
    public static final String MASTER = "master";
    public static final String CLIENT = "client";
    public static final String ADMIN = "admin";
    public static final String IS_LOGGED = "isLogged";

    //Errors
    public static final String MESSAGE_INCORRECT_LOGIN_PASSWORD = "Password or email is not correct";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String SIZES = "sizes";
    public static final String ERROR = "error";
    public static final String EMPTY_STRING = "";

    //other
    public static final String DATE_PATTERN = "dd-MM-yyyy";

    /**
     * A private constructor to prevent the instantiation of this class from external classes.
     */
    private ControllerConstants() {

    }
}
