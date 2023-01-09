package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.ADMIN;

public class AdminCommand extends Command {
    @Override
    public void process() throws ServletException, IOException {
        forward(ADMIN);
    }
}
