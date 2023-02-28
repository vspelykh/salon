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

import static ua.vspelykh.salon.util.PageConstants.JSP_PATTERN;


public class Controller extends HttpServlet {

    public static final String COMMAND = "command";

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        Command command = CommandFactory.getCommand(request);
        try (ServiceFactory serviceFactory = getServiceFactory()) {
            command.init(getServletContext(), request, response, serviceFactory);
            command.process();
        } catch (Exception e) {
            try {
                response.sendRedirect(String.format(JSP_PATTERN, "500"));
            } catch (IOException ex) {
                LOG.error("Error in FrontController");
            }
        }
    }

    public ServiceFactory getServiceFactory() {
        return new ServiceFactoryImpl();
    }
}

