package ua.vspelykh.salon.controller.command;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ua.vspelykh.salon.controller.Controller.COMMAND;

public class CommandFactory {

    private CommandFactory() {
    }

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put(CommandNames.HOME, new HomeCommand());
        commands.put(CommandNames.PRICING, new PricingCommand());
        commands.put(CommandNames.ABOUT, new AboutCommand());
        commands.put(CommandNames.MASTERS, new MastersCommand());
    }

    public static Command getCommand(HttpServletRequest request) {
        return Optional.ofNullable(commands.get(request.getParameter(COMMAND))).orElse(commands.get(CommandNames.HOME));
    }
}
