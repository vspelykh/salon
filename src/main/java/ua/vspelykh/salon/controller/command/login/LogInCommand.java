package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.controller.command.CommandNames;

import javax.servlet.ServletException;
import java.io.IOException;

public class LogInCommand extends Command {
    @Override
    public void process() throws ServletException, IOException {
        forward(CommandNames.LOGIN);
    }
}
