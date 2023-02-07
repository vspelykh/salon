package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.INVITATION;
import static ua.vspelykh.salon.dao.mapper.Column.EMAIL;
import static ua.vspelykh.salon.dao.mapper.Column.ROLE;

public class CreateInvitationCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            getServiceFactory().getInvitationService().create(request.getParameter(EMAIL), Role.valueOf(request.getParameter(ROLE)));
        } catch (ServiceException e) {
            response.sendError(500);
        }
        request.getSession().setAttribute(MESSAGE, SUCCESS + DOT +INVITATION);
        redirect(SUCCESS_REDIRECT);
    }
}
