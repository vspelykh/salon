package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.command.CommandNames.LOOK_SCHEDULE;
import static ua.vspelykh.salon.dao.mapper.Column.ID;

public class LookScheduleCommand extends AbstractScheduleCommand {

    @Override
    public void process() throws ServletException, IOException {
        try {
            setCurrentWorkingDays();
            request.setAttribute(USER, getServiceFactory().getUserService().findById(Integer.valueOf(request.getParameter(ID))));
        } catch (ServiceException e) {
            //todo
        }
        forward(LOOK_SCHEDULE);
    }


}
