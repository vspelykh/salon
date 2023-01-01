package ua.vspelykh.salon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.controller.command.CommandFactory;
import ua.vspelykh.salon.controller.command.HomeCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Controller extends HttpServlet {

    public static final String COMMAND = "command";

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getCommand(request);
        command.init(getServletContext(), request, response);
        command.process();
    }

}

