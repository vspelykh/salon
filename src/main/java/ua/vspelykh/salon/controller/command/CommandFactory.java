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
        commands.put(HOME, new HomeCommand());
        commands.put(PRICING, new PricingCommand());
        commands.put(ABOUT, new AboutCommand());
        commands.put(MASTERS, new MastersCommand());
        commands.put(LOGIN, new LogInCommand());
        commands.put(CHECK_LOGIN, new CheckLoginCommand());
        commands.put(LOGOUT, new LogoutCommand());
        commands.put(SIGN_UP_FORM, new SignUpFormCommand());
        commands.put(REGISTRATION, new RegistrationCommand());
        commands.put(SUCCESS, new SuccessCommand());
        commands.put(PROFILE, new ProfileCommand());
        commands.put(ADMIN, new AdminCommand());
        commands.put(ROLES, new RolesCommand());
        commands.put(CHANGE_ROLE, new ChangeRoleCommand());
        commands.put(CONSULTATION, new ConsultationCommand());
        commands.put(CONSULTATION_POST, new ConsultationPostCommand());
        commands.put(CONSULTATION_GET, new ConsultationGetCommand());
        commands.put(CONSULTATION_DELETE, new ConsultationDeleteCommand());
        commands.put(CALENDAR, new CalendarCommand());
        commands.put(SCHEDULE, new ScheduleCommand());
        commands.put(EDIT_SCHEDULE, new EditScheduleCommand());
        commands.put(APPOINTMENT, new AppointmentCommand());
        commands.put(CREATE_APPOINTMENT, new CreateAppointmentCommand());
        commands.put(FEEDBACK, new FeedbackCommand());
        commands.put(FEEDBACK_POST, new FeedbackPostCommand());
        commands.put(GET_SCHEDULE, new GetScheduleCommand());
        commands.put(LOOK_SCHEDULE, new LookScheduleCommand());
        commands.put(EDIT_APPOINTMENT, new EditAppointmentCommand());
    }

    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(commands.get(request.getParameter(COMMAND))).orElse(commands.get(HOME));
    }
}
