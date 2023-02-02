package ua.vspelykh.salon.util;

import ua.vspelykh.salon.model.Role;

import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.COMMAND_PARAM;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.controller.command.CommandNames.*;

public class PageConstants {

    public static final String JSP_PATTERN = "/WEB-INF/jsp/%s.jsp";
    public static final String PAGE_COMMAND_PATTERN = HOME_REDIRECT + COMMAND_PARAM + "%s";

    private static final Map<String, Set<Role>> permissions = new HashMap<>();

    static {
        Set<Role> all = new HashSet<>(List.of(Role.values()));
        Set<Role> logged = new HashSet<>(List.of(Role.ADMINISTRATOR, Role.HAIRDRESSER, Role.CLIENT));
        Set<Role> admin = new HashSet<>(List.of(Role.ADMINISTRATOR));
        Set<Role> masterAndAdmin = new HashSet<>(List.of(Role.HAIRDRESSER, Role.ADMINISTRATOR));
        Set<Role> guest = new HashSet<>(List.of(Role.GUEST));

        put(HOME_REDIRECT, all);
        put(PRICING, all);
        put(MASTERS, all);
        put(LOGIN, guest);
        put(LOGOUT, logged);
        put(CHECK_LOGIN, guest);
        put(LOGOUT, logged);
        put(SIGN_UP_FORM, guest);
        put(REGISTRATION, guest);
        put(SUCCESS, all);
        put(PROFILE, logged);
        put(ADMIN, admin);
        put(CHANGE_ROLE, admin);
        put(CONSULTATION, all);
        put(CONSULTATION_POST, all);
        put(CONSULTATION_GET, admin);
        put(CONSULTATION_DELETE, admin);
        put(CALENDAR, logged);
        put(SCHEDULE, admin);
        put(EDIT_SCHEDULE, admin);
        put(GET_SCHEDULE, masterAndAdmin);
        put(LOOK_SCHEDULE, masterAndAdmin);
        put(APPOINTMENT, logged);
        put(CREATE_APPOINTMENT, logged);
        put(FEEDBACK, logged);
        put(FEEDBACK_POST, logged);
        put(EDIT_SCHEDULE, masterAndAdmin);
        put(ORDERS, admin);
        put(CREATE_INVITATION, admin);
        put(INVITATION, admin);
    }

    public static Set<Role> getPermittedRoles(String command){
        if (command == null || command.isEmpty()) {
            command = HOME_REDIRECT;
        }
        return permissions.get(command);
    }

    private static void put(String command, Set<Role> roles){
        permissions.put(command, roles);
    }

    private PageConstants() {

    }
}
