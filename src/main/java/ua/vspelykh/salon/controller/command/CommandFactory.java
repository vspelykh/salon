package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.controller.command.appointment.*;
import ua.vspelykh.salon.controller.command.consultation.ConsultationCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationEditCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationGetCommand;
import ua.vspelykh.salon.controller.command.consultation.ConsultationPostCommand;
import ua.vspelykh.salon.controller.command.header.AboutCommand;
import ua.vspelykh.salon.controller.command.header.HomeCommand;
import ua.vspelykh.salon.controller.command.header.MastersCommand;
import ua.vspelykh.salon.controller.command.header.PricingCommand;
import ua.vspelykh.salon.controller.command.login.*;
import ua.vspelykh.salon.controller.command.response.ErrorCommand;
import ua.vspelykh.salon.controller.command.response.SuccessCommand;
import ua.vspelykh.salon.controller.command.schedule.EditScheduleCommand;
import ua.vspelykh.salon.controller.command.schedule.GetScheduleCommand;
import ua.vspelykh.salon.controller.command.schedule.LookScheduleCommand;
import ua.vspelykh.salon.controller.command.schedule.ScheduleCommand;
import ua.vspelykh.salon.controller.command.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.ADMIN;
import static ua.vspelykh.salon.controller.command.CommandNames.LOGIN;
import static ua.vspelykh.salon.controller.command.CommandNames.MASTERS;
import static ua.vspelykh.salon.controller.command.CommandNames.PROFILE;
import static ua.vspelykh.salon.controller.command.CommandNames.SUCCESS;
import static ua.vspelykh.salon.controller.command.CommandNames.*;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_404;

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
        //header
        put(ABOUT, new AboutCommand());
        put(HOME, new HomeCommand());
        put(PRICING, new PricingCommand());
        put(MASTERS, new MastersCommand());

        //appointment
        put(APPOINTMENT, new AppointmentCommand());
        put(CALENDAR, new CalendarCommand());
        put(CREATE_APPOINTMENT, new CreateAppointmentCommand());
        put(EDIT_APPOINTMENT, new EditAppointmentCommand());
        put(FEEDBACK, new FeedbackCommand());
        put(FEEDBACK_POST, new FeedbackPostCommand());
        put(ORDERS, new OrdersCommand());
        put(POSTPONE_FORM, new PostponeFormCommand());
        put(POSTPONEMENT, new PostponementCommand());

        //login
        put(CHECK_LOGIN, new CheckLoginCommand());
        put(CREATE_INVITATION, new CreateInvitationCommand());
        put(INVITATION, new InvitationFormCommand());
        put(LOGIN, new LogInCommand());
        put(LOGOUT, new LogoutCommand());
        put(REGISTRATION, new RegistrationCommand());
        put(SIGN_UP_FORM, new SignUpFormCommand());

        //response
        put(SUCCESS, new SuccessCommand());
        put(ERROR_COMMAND, new ErrorCommand());

        //user
        put(ADMIN, new AdminCommand());
        put(CHANGE_ROLE, new ChangeRoleCommand());
        put(EDIT_MASTER, new EditMasterCommand());
        put(PROFILE, new ProfileCommand());
        put(ROLES, new RolesCommand());

        //consultation
        put(CONSULTATION, new ConsultationCommand());
        put(CONSULTATION_POST, new ConsultationPostCommand());
        put(CONSULTATION_GET, new ConsultationGetCommand());
        put(CONSULTATION_EDIT, new ConsultationEditCommand());

        //schedule
        put(EDIT_SCHEDULE, new EditScheduleCommand());
        put(GET_SCHEDULE, new GetScheduleCommand());
        put(LOOK_SCHEDULE, new LookScheduleCommand());
        put(SCHEDULE, new ScheduleCommand());
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
     * HOME page command. is returned. Else if it is not mapped to any command, the ERROR page command is returned.
     *
     * @param request the HTTP request object
     * @return the corresponding Command object
     */
    public static Command getCommand(HttpServletRequest request) {
        if (request.getParameter(COMMAND) == null) {
            return COMMANDS.get(HOME);
        } else {
            Command command = Optional.ofNullable(COMMANDS.get(request.getParameter(COMMAND)))
                    .orElse(COMMANDS.get(ERROR_COMMAND));
            if (command instanceof ErrorCommand) {
                request.getSession().setAttribute(MESSAGE, ERROR_404);
            }
            return command;
        }
    }
}
