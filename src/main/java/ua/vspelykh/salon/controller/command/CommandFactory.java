package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.controller.command.login.*;
import ua.vspelykh.salon.controller.command.user.ProfileCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ua.vspelykh.salon.controller.Controller.COMMAND;
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
    }

    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(commands.get(request.getParameter(COMMAND))).orElse(commands.get(HOME));
    }
}
