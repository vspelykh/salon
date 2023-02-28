package ua.vspelykh.salon.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

public class LocalizationFilter implements Filter {
    public static final String LANG = "lang";
    private static final String LANG_REGEX = "[?&]?lang=[\\w]{2}";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getSession().getAttribute(LANG) == null) {
            req.getSession().setAttribute(LANG, UA_LOCALE);
        }
        if (req.getParameter(LANG) != null) {
            req.getSession().setAttribute(LANG, req.getParameter(LANG));
            String path = req.getContextPath() + HOME_REDIRECT;
            String queryString = req.getQueryString();
            if (queryString == null || queryString.isEmpty()) {
                res.sendRedirect(path);
            } else if (!queryString.contains("=")) {
                res.sendRedirect(HOME_REDIRECT);
            } else if (!queryString.matches(LANG_REGEX)) {
                path += "?" + queryString.replaceAll(LANG_REGEX, "");
                res.sendRedirect(path);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}