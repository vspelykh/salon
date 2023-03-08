package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.controller.command.appointment.*;
import ua.vspelykh.salon.controller.command.consultation.ConsultationCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationEditCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationGetCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationPostCommand;
import ua.vspelykh.salon.controller.command.login.*;
import ua.vspelykh.salon.controller.command.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ua.vspelykh.salon.controller.ControllerConstants.COMMAND;
import static ua.vspelykh.salon.controller.ControllerConstants.ROLES;
import static ua.vspelykh.salon.controller.command.CommandNames.*;

/**
 * The CommandFactory class is a factory that provides a command object based on the HTTP request parameter
 * "command". It holds a map of commands and initializes it with the available commands in the application.
 *
 * @version 1.0
 */
public class CommandFactory {

    /**
     * A private constructor to prevent the instantiation of this class from external classes.
     */
    private CommandFactory() {
    }

    /**
     * A map of command names and their corresponding Command objects.
     */
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        put(HOME, new HomeCommand());
        put(PRICING, new PricingCommand());
        put(ABOUT, new AboutCommand());
        put(MASTERS, new MastersCommand());
        put(LOGIN, new LogInCommand());
        put(CHECK_LOGIN, new CheckLoginCommand());
        put(LOGOUT, new LogoutCommand());
        put(SIGN_UP_FORM, new SignUpFormCommand());
        put(REGISTRATION, new RegistrationCommand());
        put(SUCCESS, new SuccessCommand());
        put(PROFILE, new ProfileCommand());
        put(ADMIN, new AdminCommand());
        put(ROLES, new RolesCommand());
        put(CHANGE_ROLE, new ChangeRoleCommand());
        put(CONSULTATION, new ConsultationCommand());
        put(CONSULTATION_POST, new ConsultationPostCommand());
        put(CONSULTATION_GET, new ConsultationGetCommand());
        put(CONSULTATION_EDIT, new ConsultationEditCommand());
        put(CALENDAR, new CalendarCommand());
        put(SCHEDULE, new ScheduleCommand());
        put(EDIT_SCHEDULE, new EditScheduleCommand());
        put(APPOINTMENT, new AppointmentCommand());
        put(CREATE_APPOINTMENT, new CreateAppointmentCommand());
        put(FEEDBACK, new FeedbackCommand());
        put(FEEDBACK_POST, new FeedbackPostCommand());
        put(GET_SCHEDULE, new GetScheduleCommand());
        put(LOOK_SCHEDULE, new LookScheduleCommand());
        put(EDIT_APPOINTMENT, new EditAppointmentCommand());
        put(ORDERS, new OrdersCommand());
        put(INVITATION, new InvitationFormCommand());
        put(CREATE_INVITATION, new CreateInvitationCommand());
        put(EDIT_MASTER, new EditMasterCommand());
        put(ERROR_COMMAND, new ErrorCommand());
    }

    /**
     * A private method that puts a command into the commands map.
     *
     * @param commandName the name of the command
     * @param command     the corresponding command object
     */
    private static void put(String commandName, Command command) {
        COMMANDS.put(commandName, command);
    }

    /**
     * Returns a Command object based on the HTTP request parameter "command". If the parameter does not exist
     * or is not mapped to any command, the HOME command is returned.
     *
     * @param request the HTTP request object
     * @return the corresponding Command object
     */
    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(COMMANDS.get(request.getParameter(COMMAND))).orElse(COMMANDS.get(HOME));
    }
}
