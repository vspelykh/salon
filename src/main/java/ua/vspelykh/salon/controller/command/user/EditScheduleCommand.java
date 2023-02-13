package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.SalonUtils.getTime;

public class EditScheduleCommand extends AbstractScheduleCommand {

    @Override
    public void process() throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter(ID));
        try {
            String[] datesArray = request.getParameter(DAYS).split(", ");
            if (SAVE.equals(request.getParameter(ACTION))) {
                Time timeStart = getTime(request.getParameter(TIME_START));
                Time timeEnd = getTime(request.getParameter(TIME_END));
                getServiceFactory().getWorkingDayService().save(userId, datesArray, timeStart, timeEnd);
            } else if (DELETE.equals(request.getParameter(ACTION))) {
                getServiceFactory().getWorkingDayService().deleteWorkingDaysByUserIdAndDatesArray(userId, datesArray);
            }
        } catch (ServiceException e) {
            response.sendError(500);
        }
        redirect(SCHEDULE_REDIRECT + userId);
    }


}
