package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.service.InvitationService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.COMMAND_PARAM;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.controller.command.CommandNames.ADMIN;
import static ua.vspelykh.salon.dao.mapper.Column.EMAIL;
import static ua.vspelykh.salon.dao.mapper.Column.ROLE;

public class CreateInvitationCommand extends Command {

    private final InvitationService invitationService = ServiceFactory.getInvitationService();
    @Override
    public void process() throws ServletException, IOException {

        try {
            invitationService.create(request.getParameter(EMAIL), Role.valueOf(request.getParameter(ROLE)));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        redirect(HOME_REDIRECT + COMMAND_PARAM + ADMIN);
    }
}
