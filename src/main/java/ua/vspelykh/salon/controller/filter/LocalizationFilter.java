package ua.vspelykh.salon.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;

public class LocalizationFilter implements Filter {
    public static final String LANG = "lang";
    private static final String regex = "[?&]?lang=[\\w]{2}";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getSession().getAttribute(LANG) == null){
            req.getSession().setAttribute(LANG, "en");
        }

        if (req.getParameter(LANG) != null) {
            req.getSession().setAttribute(LANG, req.getParameter(LANG));
            String path = req.getContextPath() + HOME_REDIRECT;
            if (!req.getQueryString().matches(regex)) {
                path += "?" + req.getQueryString().replaceAll(regex, "");
            }
            res.sendRedirect(path);
            return;
        }
        chain.doFilter(request, response);
    }
}