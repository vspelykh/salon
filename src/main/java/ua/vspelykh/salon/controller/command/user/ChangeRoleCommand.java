package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.dao.mapper.Column.USER_ID;

public class ChangeRoleCommand extends Command {
    public static String REMOVE = "remove";
    public static String ADD = "add";

    @Override
    public void process() throws ServletException, IOException {
        Role role = Role.valueOf(request.getParameter(ROLE));
        String action = request.getParameter(ACTION);
        int userId = Integer.parseInt(request.getParameter(USER_ID));
        try {
            getServiceFactory().getUserService().updateRole(userId, action, role);
            User user = getServiceFactory().getUserService().findById(userId);
            request.getSession().setAttribute(MESSAGE, SUCCESS);
            redirect(context.getContextPath() + HOME_REDIRECT +
                    "?command=roles&message=success&search=" + user.getEmail());
        } catch (ServiceException e) {
            //TODO
        }
    }
}
