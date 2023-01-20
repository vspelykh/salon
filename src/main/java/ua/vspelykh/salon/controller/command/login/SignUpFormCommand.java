package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.SIGN_UP;

public class SignUpFormCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        forward(SIGN_UP);
    }
}
