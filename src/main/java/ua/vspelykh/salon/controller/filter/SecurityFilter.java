package ua.vspelykh.salon.controller.filter;

import ua.vspelykh.salon.model.Role;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static ua.vspelykh.salon.controller.Controller.COMMAND;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.util.PageConstants.PAGE_COMMAND_PATTERN;
import static ua.vspelykh.salon.util.PageConstants.getPermittedRoles;

public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean userIsGuest = checkIfSessionHasRolesAttr(req);

        String command = req.getParameter(COMMAND);
        Set<Role> roles = getPermittedRoles(command);

        if (roles != null) {
            HttpSession session = req.getSession();
            if (session != null) {
                Set<Role> rolesInSession = (Set<Role>) session.getAttribute(ROLES);
                if (rolesInSession != null && checkUserContainsAnyPermissionRole(rolesInSession, roles)) {
                    chain.doFilter(request, response);
                    return;
                }
                if (!Objects.requireNonNull(rolesInSession).contains(Role.GUEST)) {
                    userIsGuest = false;
                }
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
        if (userIsGuest) {
            res.sendRedirect(String.format(PAGE_COMMAND_PATTERN, LOGIN));
        } else if (command.equals(LOGIN) || command.equals(SIGN_UP)) {
            res.sendRedirect(HOME_REDIRECT);
        } else {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }


    private boolean checkUserContainsAnyPermissionRole(Set<Role> rolesInSession, Set<Role> roles) {
        return !Collections.disjoint(rolesInSession, roles);
    }

    private boolean checkIfSessionHasRolesAttr(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Set<Role> userRoles = (Set<Role>) session.getAttribute(ROLES);
        if (Objects.isNull(userRoles)) {
            userRoles = new HashSet<>(List.of(Role.GUEST));
            session.setAttribute(ROLES, userRoles);
            return true;
        }
        return userRoles.contains(Role.GUEST);
    }
}
