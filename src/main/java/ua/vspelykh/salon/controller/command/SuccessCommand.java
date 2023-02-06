package ua.vspelykh.salon.controller.command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class SuccessCommand extends Command {
    @Override
    public void process() throws ServletException, IOException {
        if (request.getSession().getAttribute(MESSAGE) != null) {
            forward(SUCCESS);
        }
        else redirect(HOME_REDIRECT);
    }
}
