package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.RolePermissions.JSP_PATTERN;

/**
 * This abstract class serves as a base for all the command classes used in the web application.
 * It provides the necessary methods and attributes that are common to all command classes.
 *
 * @version 1.0
 **/
public abstract class Command {

    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServiceFactory serviceFactory;

    /**
     * Initializes the attributes of the command object with the provided values.
     *
     * @param servletContext  object for the web application
     * @param servletRequest  object for the current request
     * @param servletResponse object for the current request
     * @param serviceFactory: the ServiceFactory object used to access the services of the web application
     */
    public void init(
            ServletContext servletContext,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, ServiceFactory serviceFactory) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
        this.serviceFactory = serviceFactory;
    }

    /**
     * An abstract method that needs to be implemented by each concrete command class.
     *
     * @throws ServletException - a general exception a servlet can throw when it encounters difficulty.
     * @throws IOException      if any I/O error occurs.
     */
    public abstract void process() throws ServletException, IOException;

    /**
     * Forwards the request to the provided target JSP file
     *
     * @param target - name of the page.
     * @throws ServletException - a general exception a servlet can throw when it encounters difficulty.
     * @throws IOException      if any I/O error occurs.
     */
    protected void forward(String target) throws ServletException, IOException {
        target = String.format(JSP_PATTERN, target);
        RequestDispatcher dispatcher = request.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }

    /**
     * Redirects the request to the provided target URL.
     *
     * @param target - URL to redirect.
     * @throws IOException if any I/O error occurs.
     */
    protected void redirect(String target) throws IOException {
        response.sendRedirect(context.getContextPath() + target);
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return String value of the parameter or null if parameter is absent.
     */
    public String getParameter(String param) {
        return request.getParameter(param);
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return String value of the parameter and null if param value is null or empty.
     */
    public String getParameterElseNull(String param) {
        String parameterValue = getParameter(param);
        return parameterValue != null && !parameterValue.isEmpty() ? parameterValue : null;
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return int value of the parameter or null if absent.
     */
    protected Integer getParameterInt(String param)  {
        if (isParameterNull(param)) {
            return null;
        }
        String value = getParameter(param);
        return Integer.parseInt(value);

    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return LocalDate value of the parameter or null if absent.
     */
    public LocalDate getParameterLocalDate(String param) {
        if (isParameterNull(param)) {
            return null;
        }
        String value = getParameter(param);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        try {
            return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(value);
        }
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return LocalTime value of the parameter or null if absent.
     */
    public LocalTime getParameterLocalTime(String param) {
        if (isParameterNull(param)) {
            return null;
        }
        String value = getParameter(param);
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @param param - name of the parameter.
     * @return Time value of the parameter.
     */
    public Time getParameterTime(String param) {
        String value = getParameter(param);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Time.valueOf(LocalTime.parse(value));
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @return int value of the page. Returns first page, if value is null.
     */
    protected int getPageParameter() {
        return request.getParameter(PAGE) == null ? DEFAULT_PAGE : Integer.parseInt(request.getParameter(PAGE));
    }

    /**
     * Retrieves the value of a request parameter with the provided name.
     *
     * @return int value of the size. Returns default (5) value, if value is null.
     */
    protected int getSizeParameter() {
        return request.getParameter(SIZE) == null ? DEFAULT_SIZE : Integer.parseInt(request.getParameter(SIZE));
    }

    /**
     * Sets the pagination attributes for the request based on the provided size and count of items.
     *
     * @param size         - number of items on one page.
     * @param countOfItems - total value of items.
     */
    protected void setPaginationAttrs(int size, int countOfItems) {
        int[] pages = new int[(int) Math.ceil(countOfItems * 1.0 / size)];
        for (int i = 1, j = 0; i <= pages.length; j++, i++) {
            pages[j] = i;
        }
        request.setAttribute(LAST_PAGE, pages.length);
        request.setAttribute(PAGES_ARRAY, pages);
        request.setAttribute(NUMBER_OF_PAGES, (int) Math.ceil(countOfItems * 1.0 / size));
        String path = "?" + request.getQueryString().replaceAll(PAGE_PARAM_REGEX, "");
        request.setAttribute(PATH_STR, path);
    }

    /**
     * Checks if the given request parameter is  null or an empty string.
     *
     * @param paramName the name of the request parameter to check
     * @return true if the parameter is null or an empty string, false otherwise
     */
    protected boolean isParameterNull(String paramName) {
        return getParameter(paramName) == null || getParameter(paramName).isEmpty();
    }

    /**
     * Returns the locale associated with the current session.
     *
     * @return a String representing the locale associated with the current session.
     */
    protected String getLocale() {
        Object langAttribute = request.getSession().getAttribute(LANG);
        if (langAttribute == null) {
            return UA_LOCALE;
        }
        return langAttribute.toString();
    }

    /**
     * Returns the user object representing the currently logged-in user associated with the current session.
     *
     * @return a User object representing the currently logged-in user associated with the current session.
     */
    protected User getCurrentUser() {
        return (User) request.getSession().getAttribute(CURRENT_USER);
    }

    /**
     * Sends a 404 error response to the client.
     *
     * @throws IOException if any I/O error occurs.
     */
    protected void sendError404() throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Sends a 403 error response to the client.
     *
     * @throws IOException if any I/O error occurs.
     */
    protected void sendError403() throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Sends a 500 error response to the client.
     *
     * @throws IOException if any I/O error occurs.
     */
    protected void sendError500() throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Sets the value of an attribute in the current HTTP session.
     *
     * @param name  The name of the attribute to be set.
     * @param value The value of the attribute to be set.
     */
    protected void setSessionAttribute(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
     * Sets the value of an attribute in the current HTTP request.
     *
     * @param name  The name of the attribute to be set.
     * @param value The value of the attribute to be set.
     */
    protected void setRequestAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Access to service.
     *
     * @return the ServiceFactory object used by the command object
     */
    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
}