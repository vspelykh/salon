package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.controller.command.appointment.*;
import ua.vspelykh.salon.controller.command.login.*;
import ua.vspelykh.salon.controller.command.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ua.vspelykh.salon.controller.Controller.COMMAND;
import static ua.vspelykh.salon.controller.ControllerConstants.ROLES;
import static ua.vspelykh.salon.controller.command.CommandNames.*;

public class CommandFactory {

    private CommandFactory() {
    }

    private static final Map<String, Command> commands = new HashMap<>();

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
        put(CONSULTATION_DELETE, new ConsultationDeleteCommand());
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
    }

    private static void put(String commandName, Command command){
        commands.put(commandName, command);
    }

    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(commands.get(request.getParameter(COMMAND))).orElse(commands.get(HOME));
    }
}
