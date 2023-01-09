package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.controller.command.appointment.ConsultationCommand;
import ua.vspelykh.salon.controller.command.appointment.ConsultationDeleteCommand;
import ua.vspelykh.salon.controller.command.appointment.ConsultationGetCommand;
import ua.vspelykh.salon.controller.command.appointment.ConsultationPostCommand;
import ua.vspelykh.salon.controller.command.login.*;
import ua.vspelykh.salon.controller.command.user.AdminCommand;
import ua.vspelykh.salon.controller.command.user.ChangeRoleCommand;
import ua.vspelykh.salon.controller.command.user.ProfileCommand;
import ua.vspelykh.salon.controller.command.user.RolesCommand;

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
    }

    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(commands.get(request.getParameter(COMMAND))).orElse(commands.get(HOME));
    }
}
