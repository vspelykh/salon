package ua.vspelykh.salon.controller.command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.HOME_PAGE;

public class HomeCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        forward(HOME_PAGE);
    }
}