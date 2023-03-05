package ua.vspelykh.salon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.controller.command.CommandFactory;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.impl.ServiceFactoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.vspelykh.salon.util.exception.Messages.CONTROLLER_ERROR;

/**
 * The Controller class is a front controller Servlet responsible for handling all requests to the web application.
 * It implements the Front Controller pattern, where all requests are first directed to this servlet, which then delegates
 * request processing to individual commands.
 * <p>
 * This class contains the main logic of the application and handles both GET and POST requests by invoking the appropriate
 * command object based on the request parameter. It also provides a method to create a ServiceFactory instance.
 *
 * @version 1.0
 */
public class Controller extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    /**
     * Handles all GET requests by invoking the process method.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    /**
     * Handles all POST requests by invoking the process method.
     *
     * @param req  the HTTP request
     * @param resp the HTTP response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        process(req, resp);
    }

    /**
     * This method processes the incoming request by delegating it to the appropriate command object.
     * It initializes the command object with the required parameters, creates a ServiceFactory instance,
     * and invokes the command's process method. It catches any exceptions that might be thrown and returns
     * an error response.
     * <p>
     * It also closes the connection to the database after invoking the Command's process method.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    private void process(HttpServletRequest request, HttpServletResponse response) {
        Command command = CommandFactory.getCommand(request);
        try (ServiceFactory serviceFactory = getServiceFactory()) {
            command.init(getServletContext(), request, response, serviceFactory);
            command.process();
        } catch (Exception e) {
            try {
                LOG.error(e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ex) {
                LOG.error(CONTROLLER_ERROR);
            }
        }
    }

    /**
     * This method creates and returns a new instance of ServiceFactoryImpl.
     *
     * @return a new ServiceFactory instance
     */
    public ServiceFactory getServiceFactory() {
        return new ServiceFactoryImpl();
    }
}

