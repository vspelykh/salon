package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashSet;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class LogoutCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        request.getSession().removeAttribute(ROLES);
        request.getSession().removeAttribute(IS_LOGGED);
        request.getSession().removeAttribute(CURRENT_USER);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.GUEST);
        request.getSession().setAttribute(ROLES, roles);
        redirect(context.getContextPath() + HOME_REDIRECT);
    }
}
