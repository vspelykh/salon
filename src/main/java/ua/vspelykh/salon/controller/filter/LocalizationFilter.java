package ua.vspelykh.salon.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.EMPTY_STRING;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

/**
 * A filter that sets the user's language preference and ensures that the language query parameter is present and valid.
 *
 * @version 1.0
 */
public class LocalizationFilter implements Filter {
    public static final String LANG = "lang";
    private static final String LANG_REGEX = "[?&]?lang=[\\w]{2}";

    /**
     * Sets the user's language preference and ensures that the language query parameter is present and valid.
     * If the "lang" parameter is not present, the user's session language preference is used (defaulting to "ua").
     * If the "lang" parameter is present and valid, the user's session language preference is updated accordingly.
     * If the "lang" parameter is present but invalid, it is removed from the query string and the user is redirected
     * to the same page without the invalid parameter.
     *
     * @param request  the ServletRequest object containing the request information.
     * @param response the ServletResponse object containing the response information.
     * @param chain    the FilterChain object used to invoke the next filter or servlet in the chain.
     * @throws IOException      if an I/O error occurs while processing the request.
     * @throws ServletException if a servlet error occurs while processing the request.
     */
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
                path += "?" + queryString.replaceAll(LANG_REGEX, EMPTY_STRING);
                res.sendRedirect(path);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}