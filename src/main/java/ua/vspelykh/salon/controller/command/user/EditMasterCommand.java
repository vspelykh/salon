package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.exception.Messages.EDIT_MASTER_ERROR;

/**
 * The EditMasterCommand class extends the abstract Command class and is responsible for processing the editing of a
 * master's profile.
 *
 * @version 1.0
 */
public class EditMasterCommand extends Command {

    /**
     * This method processes the editing of a master's profile.
     * It receives and validates the updated master's information from the form submission.
     * If the information is valid, it updates the database with the new information and redirects the user to the
     * master's schedule page.
     * If there is an error during the processing, it sends a 500 error response.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            int masterId = getParameterInt(ID);
            MastersLevel level = MastersLevel.valueOf(getParameter(LEVEL));
            String about = getParameter(ABOUT);
            String aboutUa = getParameter(ABOUT + UA);
            boolean active = Boolean.parseBoolean(getParameter(ACTIVE));
            UserLevel userLevel = new UserLevel(masterId, level, about, aboutUa, active);
            serviceFactory.getUserService().update(userLevel);
            redirect(SCHEDULE_REDIRECT + masterId);
        } catch (ServiceException e) {
            setSessionAttribute(MESSAGE, EDIT_MASTER_ERROR);
            redirect(ERROR_REDIRECT);
        }
    }
}
