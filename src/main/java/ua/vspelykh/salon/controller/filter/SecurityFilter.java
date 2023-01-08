package ua.vspelykh.salon.controller.filter;

import ua.vspelykh.salon.model.Role;

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

public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Set<Role> roles = (Set<Role>) session.getAttribute(ROLES);
        if (Objects.isNull(roles)) {
            roles = new HashSet<>();
            roles.add(Role.GUEST);
            session.setAttribute(ROLES, roles);
        }
        chain.doFilter(request, response);
//        List<String> accessibleUrls = urls.get(role);
//        if (accessibleUrls.contains(path)) {
//            res.sendRedirect(ViewConstants.Paths.Login);
//        } else {
//            chain.doFilter(req, res);
//        }

    }
}
