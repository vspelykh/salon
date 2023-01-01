package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.impl.UserDaoImpl;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.DaoException;

import javax.servlet.ServletException;
import java.io.IOException;

public class HomeCommand extends Command {

    private static final String page = "index";

    @Override
    public void process() throws ServletException, IOException {
        UserDao us = new UserDaoImpl();
        try {
            request.setAttribute("users", us.findAll());

        } catch (DaoException e) {
//            LOG
        }
        forward(page);
    }
}
