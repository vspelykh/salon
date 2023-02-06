package ua.vspelykh.salon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.controller.command.CommandFactory;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.impl.ServiceFactoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Controller extends HttpServlet {

    public static final String COMMAND = "command";

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getCommand(request);
        try(ServiceFactory serviceFactory = getServiceFactory()){
            command.init(getServletContext(), request, response, serviceFactory);
            command.process();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public ServiceFactory getServiceFactory() {
        return new ServiceFactoryImpl();
    }
}
