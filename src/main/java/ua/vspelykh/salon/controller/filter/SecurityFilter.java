package ua.vspelykh.salon.controller.filter;

import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.util.PageConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class SecurityFilter implements Filter {


    private HashMap<Role, List<String>> urls = new HashMap<>();

    {
        urls.put(Role.ADMINISTRATOR, PageConstants.adminUrls);
        urls.put(Role.HAIRDRESSER, PageConstants.masterUrls);
        urls.put(Role.CLIENT, PageConstants.clientUrls);
        urls.put(Role.GUEST, PageConstants.guestUrls);
    }



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
