package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.ACTION;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.dao.mapper.Column.USER_ID;

public class ChangeRoleCommand extends Command {
    public static String REMOVE = "remove";
    public static String ADD = "add";

    private UserService userService = ServiceFactory.getUserService();

    @Override
    public void process() throws ServletException, IOException {
        Role role = Role.valueOf(request.getParameter(ROLE));
        String action = request.getParameter(ACTION);
        int userId = Integer.parseInt(request.getParameter(USER_ID));
        try {
            userService.updateRole(userId, action, role);
            User user = userService.findById(userId);
            redirect(context.getContextPath() + HOME_REDIRECT +
                    "?command=roles&message=success&search=" + user.getEmail());
        } catch (ServiceException e) {
            //TODO
        }
    }
}
