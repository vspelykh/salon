package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.service.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.util.PageConstants.JSP_PATTERN;
import static ua.vspelykh.salon.util.SalonUtils.parseLocalDate;

public abstract class Command {

    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServiceFactory serviceFactory;

    public void init(
            ServletContext servletContext,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, ServiceFactory serviceFactory) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
        this.serviceFactory = serviceFactory;
    }

    public abstract void process() throws ServletException, IOException;

    protected void forward(String target) throws ServletException, IOException {
        target = String.format(JSP_PATTERN, target);
        RequestDispatcher dispatcher = request.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }

    protected void redirect(String target) throws IOException {
        response.sendRedirect(target);
    }

    protected boolean checkNullParam(String param) {
        return param != null && !param.isEmpty();
    }

    protected void countAndSet(int size, int countOfItems) {
        int[] pages = new int[(int) Math.ceil(countOfItems * 1.0 / size)];
        for (int i = 1, j = 0; i <= pages.length; j++, i++) {
            pages[j] = i;
        }
        request.setAttribute(LAST_PAGE, pages.length);
        request.setAttribute(PAGES_ARRAY, pages);
        request.setAttribute(NUMBER_OF_PAGES, (int) Math.ceil(countOfItems * 1.0 / size));
        String path = "?" + request.getQueryString().replaceAll("&page=[0-9]*", "");
        request.setAttribute(PATH_STR, path);
    }

    protected Integer checkAndGetIntegerParam(String param) {
        return request.getParameter(param) == null || request.getParameter(param).isEmpty()
                ? null : Integer.parseInt(request.getParameter(param));
    }

    protected LocalDate checkAndGetLocalDateParam(String param) {
        return request.getParameter(param) == null || request.getParameter(param).isEmpty()
                ? null : parseLocalDate(String.valueOf(request.getParameter(param)));
    }

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
}