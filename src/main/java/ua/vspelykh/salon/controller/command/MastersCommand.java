package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

public class MastersCommand extends Command {

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            request.setAttribute("masters", userService.findMasters(true));
            forward("masters");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
