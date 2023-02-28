package ua.vspelykh.salon.controller.filter;

import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static ua.vspelykh.salon.controller.Controller.COMMAND;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.LOGIN;
import static ua.vspelykh.salon.controller.command.CommandNames.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.entity.Role.ADMINISTRATOR;
import static ua.vspelykh.salon.model.entity.Role.HAIRDRESSER;
import static ua.vspelykh.salon.util.PageConstants.PAGE_COMMAND_PATTERN;
import static ua.vspelykh.salon.util.PageConstants.getPermittedRoles;

public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean userIsGuest = checkIfSessionHasRolesAttr(req);

        String command = req.getParameter(COMMAND);
        if (logoutIfUserIsBanned(req, res)) return;

        Set<Role> roles = getPermittedRoles(command);

        HttpSession session = req.getSession();
        if (session != null) {
            User currentUser = (User) session.getAttribute(CURRENT_USER);
            Set<Role> rolesInSession = currentUser.getRoles();
            if (rolesInSession != null && checkUserContainsAnyPermissionRole(rolesInSession, roles)) {
                if (checkAccessForMasterToSchedulePages(req, res, command)) return;
                chain.doFilter(request, response);
                return;
            }
            if (!Objects.requireNonNull(rolesInSession).contains(Role.GUEST)) {
                userIsGuest = false;
            }
        }
        isUserIsGuest(req, res, userIsGuest, command);
    }

    private boolean logoutIfUserIsBanned(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (isUserBanned(req)) {
            logout(req, res);
            return true;
        }
        return false;
    }

    private boolean checkAccessForMasterToSchedulePages(HttpServletRequest req, HttpServletResponse res, String command) throws IOException {
        if (command != null && (command.equals(GET_SCHEDULE) || command.equals(LOOK_SCHEDULE))
                && !isCurrentUserIsMasterAndIdIsEqual(req)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return true;
        }
        return false;
    }

    private void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.getSession().removeAttribute(IS_LOGGED);
        req.getSession().removeAttribute(CURRENT_USER);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.GUEST);
        User guestUser = User.builder().roles(roles).build();
        req.getSession().setAttribute(CURRENT_USER, guestUser);
        res.sendRedirect(req.getServletContext().getContextPath() + HOME_REDIRECT);
    }

    private boolean isUserBanned(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        if (user.getRoles().contains(Role.GUEST)) {
            return false;
        }
        return user.getRoles().isEmpty();
    }

    private boolean isCurrentUserIsMasterAndIdIsEqual(HttpServletRequest req) {
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        if (currentUser.getRoles().contains(ADMINISTRATOR)) {
            return true;
        }
        boolean isMaster = currentUser.getRoles().contains(HAIRDRESSER);
        return isMaster && Integer.valueOf(req.getParameter(ID)).equals(currentUser.getId());
    }


    private boolean checkUserContainsAnyPermissionRole(Set<Role> rolesInSession, Set<Role> roles) {
        return !Collections.disjoint(rolesInSession, roles);
    }

    private boolean checkIfSessionHasRolesAttr(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        Set<Role> rolesInSession = currentUser.getRoles();
        if (Objects.isNull(rolesInSession)) {
            currentUser.setRoles(new HashSet<>(List.of(Role.GUEST)));
            session.setAttribute(CURRENT_USER, currentUser);
            return true;
        }
        return rolesInSession.contains(Role.GUEST);
    }

    private void isUserIsGuest(HttpServletRequest req, HttpServletResponse res, boolean userIsGuest, String command) throws IOException {
        if (userIsGuest) {
            String path = HOME_REDIRECT + "?" + req.getQueryString();
            req.getSession().setAttribute(LAST_PAGE, path);
            res.sendRedirect(String.format(PAGE_COMMAND_PATTERN, LOGIN));
        } else if (command.equals(LOGIN) || command.equals(SIGN_UP)) {
            res.sendRedirect(HOME_REDIRECT);
        } else {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }
}
