package ua.vspelykh.salon.controller.filter;

import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.LOGIN;
import static ua.vspelykh.salon.controller.command.CommandNames.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.entity.Role.ADMINISTRATOR;
import static ua.vspelykh.salon.model.entity.Role.HAIRDRESSER;
import static ua.vspelykh.salon.util.PageConstants.PAGE_COMMAND_PATTERN;
import static ua.vspelykh.salon.util.PageConstants.getPermittedRoles;

/**
 * The SecurityFilter class is an implementation of the Filter interface, responsible for filtering incoming requests
 * and restricting access to certain resources based on the user's role.
 *
 * @version 1.0
 */
public class SecurityFilter implements Filter {

    /**
     * Filters incoming requests and restricts access to certain resources based on the user's role.
     * Checks if the user has the required permissions to access a resource and redirects to the login page if the user
     * is a guest or does not have sufficient privileges.
     * <p>
     * Also handles user banning and logging out, and checks if the current user is a master and has the necessary
     * permissions to access schedule-related pages.
     *
     * @param request  the ServletRequest object containing the request information.
     * @param response the ServletResponse object containing the response information.
     * @param chain    the FilterChain object used to invoke the next filter in the chain.
     * @throws IOException      if an I/O error occurs while processing the request.
     * @throws ServletException if an error occurs during the processing of the request.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean userIsGuest = checkIfSessionHasRolesAttr(req);

        String command = req.getParameter(COMMAND);
        if (logoutIfUserIsBanned(req, res)) return;

        Set<Role> roles = getPermittedRoles(command);
        if (roles == null) {
            chain.doFilter(request, response);
            return;
        }

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

    /**
     * Logs out the user and redirects to the login page if the user is banned.
     *
     * @param req the HttpServletRequest object containing the request information.
     * @param res the HttpServletResponse object containing the response information.
     * @return true if the user is banned and has been logged out, false otherwise.
     * @throws IOException if an I/O error occurs while processing the request.
     */
    private boolean logoutIfUserIsBanned(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (isUserBanned(req)) {
            logout(req, res);
            return true;
        }
        return false;
    }

    /**
     * Checks if the current user is a master and has the necessary permissions to access schedule-related pages.
     * If the user is not a master or does not have the necessary permissions, sends an error response and returns true.
     *
     * @param req     the HttpServletRequest object containing the request information.
     * @param res     the HttpServletResponse object containing the response information.
     * @param command name of command to control permission.
     */
    private boolean checkAccessForMasterToSchedulePages(HttpServletRequest req, HttpServletResponse res, String command) throws IOException {
        if (command != null && (command.equals(GET_SCHEDULE) || command.equals(LOOK_SCHEDULE))
                && !isCurrentUserIsMasterAndIdIsEqual(req)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
            return true;
        }
        return false;
    }

    /**
     * Logs out the current user by invalidating the session and redirecting to the login page.
     *
     * @param req the HttpServletRequest object containing the request information.
     * @param res the HttpServletResponse object containing the response information.
     * @throws IOException if an I/O error occurs while processing the request.
     */
    private void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.getSession().removeAttribute(IS_LOGGED);
        req.getSession().removeAttribute(CURRENT_USER);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.GUEST);
        User guestUser = User.builder().roles(roles).build();
        req.getSession().setAttribute(CURRENT_USER, guestUser);
        res.sendRedirect(req.getServletContext().getContextPath() + HOME_REDIRECT);
    }

    /**
     * Checks if the current user is banned.
     *
     * @return true if the current user has no roles, false otherwise.
     */
    private boolean isUserBanned(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(CURRENT_USER);
        if (user.getRoles().contains(Role.GUEST)) {
            return false;
        }
        return user.getRoles().isEmpty();
    }

    /**
     * Checks if the current user is a master user and has the same ID as the user whose data is being modified.
     *
     * @param req the HttpServletRequest object containing the request information.
     * @return true if the current user is a master user and has the same ID as the user being modified, false otherwise.
     */
    private boolean isCurrentUserIsMasterAndIdIsEqual(HttpServletRequest req) {
        User currentUser = (User) req.getSession().getAttribute(CURRENT_USER);
        if (currentUser.getRoles().contains(ADMINISTRATOR)) {
            return true;
        }
        boolean isMaster = currentUser.getRoles().contains(HAIRDRESSER);
        return isMaster && Integer.valueOf(req.getParameter(ID)).equals(currentUser.getId());
    }

    /**
     * Checks if the current user has any of the specified permission roles.
     *
     * @param rolesInSession the list of user roles,
     * @param roles          the list of roles that allowed for current command.
     * @return true if the current user has any of the specified roles, false otherwise.
     */
    private boolean checkUserContainsAnyPermissionRole(Set<Role> rolesInSession, Set<Role> roles) {
        return !Collections.disjoint(rolesInSession, roles);
    }

    /**
     * Checks if the current user session has the "roles" attribute set.
     *
     * @param req the HttpServletRequest object containing the request information.
     * @return true if the "roles" attribute is set in the current session, false otherwise.
     */
    private boolean checkIfSessionHasRolesAttr(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute(CURRENT_USER);
        if (currentUser == null) {
            currentUser = User.builder().build();
        }
        Set<Role> rolesInSession = currentUser.getRoles();
        if (Objects.isNull(rolesInSession)) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.GUEST);
            currentUser.setRoles(roles);
            session.setAttribute(CURRENT_USER, currentUser);
            return true;
        }
        return rolesInSession.contains(Role.GUEST);
    }

    /**
     * Checks if the user is a guest, and redirects to the login page if they are. If the user is not a guest
     * and the command is LOGIN or SIGN_UP, redirects to the home page. If the user is not a guest and the command
     * is not LOGIN or SIGN_UP, sends a 403 (FORBIDDEN) error with an "Access denied" message.
     *
     * @param req         the HttpServletRequest object containing the request information.
     * @param res         the HttpServletResponse object containing the response information.
     * @param userIsGuest a boolean indicating if the user is a guest or not.
     * @param command     a String indicating the command being requested.
     * @throws IOException if an I/O error occurs while processing the request.
     */
    private void isUserIsGuest(HttpServletRequest req, HttpServletResponse res, boolean userIsGuest, String command) throws IOException {
        if (userIsGuest) {
            String path = HOME_REDIRECT + "?" + req.getQueryString();
            req.getSession().setAttribute(LAST_PAGE, path);
            res.sendRedirect(String.format(PAGE_COMMAND_PATTERN, LOGIN));
        } else if (command.equals(LOGIN) || command.equals(SIGN_UP)) {
            res.sendRedirect(HOME_REDIRECT);
        } else {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
        }
    }
}
