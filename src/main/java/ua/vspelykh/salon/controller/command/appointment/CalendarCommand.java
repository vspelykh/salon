package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.CALENDAR;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.SalonUtils.getStringDate;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;

/**
 * The CalendarCommand class extends the Command class and provides information about the days and time slots
 * available for appointment. It contains several methods that set different attributes to the request object based
 * on requested user's ID, and the date selected in the calendar.
 *
 * @version 1.0
 */
public class CalendarCommand extends Command {

    /**
     * This method is responsible for processing the request and setting the attributes to the request object.
     * It gets the current user's ID and the requested user's ID. If they are the same, the user is redirected to the
     * masters page. Otherwise, it calls methods to set the attributes related to the requested user's ID.
     * Finally, it forwards the request to the calendar page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            int userId = getCurrentUser().getId();
            int requestedUserId = getParameterInt(ID);
            if (userId == requestedUserId) {
                redirect(MASTERS_REDIRECT);
                return;
            }
            setMasterInfoAttrs(requestedUserId);
            setFeedbacksAttrs(requestedUserId);
            setTimeSlotsAttrs(requestedUserId);
            forward(CALENDAR);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Sets the attributes related to the requested user's information. It calls the findDaysByUserId method
     * to get the user's working days, sets the days attribute to the list of working days, and sets the user attribute
     * to the user's details obtained by calling the findById method.
     *
     * @param requestedUserId The ID of the requested user
     * @throws ServiceException If there is an issue with the service layer
     */
    private void setMasterInfoAttrs(int requestedUserId) throws ServiceException {
        List<WorkingDay> workingDays = serviceFactory.getWorkingDayService().findDaysByUserId(requestedUserId);
        setRequestAttribute(DAYS, workingDays);
        setRequestAttribute(USER, serviceFactory.getUserService().findById(requestedUserId));
    }

    /**
     * This method sets the available time slots for the requested user. If a date is selected, it gets the working day
     * details for that date using the getByUserIdAndDate method and adds the available time slots
     * to the request attributes. If no date is selected, it sets the placeholder text based on the locale.
     *
     * @param requestedUserId The ID of the requested user
     * @throws ServiceException If there is an issue with the service layer
     */
    private void setTimeSlotsAttrs(int requestedUserId) throws ServiceException {
        if (request.getParameter(DAY) != null) {
            WorkingDay day = serviceFactory.getWorkingDayService().getByUserIdAndDate(requestedUserId,
                    getParameterLocalDate(DAY));
            setRequestAttribute(DAY, day);
            setRequestAttribute(PLACEHOLDER, getStringDate(day.getDate()));
            addTimeSlotsToAttributes(day);
        } else {
            String placeholder = getLocale().equals(UA_LOCALE) ? PLACEHOLDER_UA : PLACEHOLDER_EN;
            setRequestAttribute(PLACEHOLDER, placeholder);
        }
    }

    /**
     * This method sets the available time slots for the requested user. If a specific date is selected in the calendar,
     * it gets the user's working day details for that date and adds the available time slots to the request attributes.
     * If no date is selected, it sets the placeholder text based on the locale.
     *
     * @param day The WorkingDay represents the working day details for a specific date of the requested user.
     * @throws ServiceException If there is an issue with the service layer
     */
    private void addTimeSlotsToAttributes(WorkingDay day) throws ServiceException {
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId()), INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        setRequestAttribute(SLOTS, slots);
    }

    /**
     * This method sets the feedbacks for the requested user. It gets the feedbacks from the service layer and adds
     * them to the request attributes. It also sets the current page number and the total count of feedbacks.
     * Finally, it sets the pagination attributes to allow navigation between feedback pages.
     *
     * @param requestedUserId - the ID of the requested user
     * @throws ServiceException if there is an issue with the service layer
     */
    private void setFeedbacksAttrs(int requestedUserId) throws ServiceException {
        int page = getPageParameter();
        List<FeedbackDto> feedbacks = serviceFactory.getFeedbackService().getFeedbacksByMasterId(requestedUserId, page);
        setRequestAttribute(FEEDBACKS, feedbacks);
        setRequestAttribute(PAGE + CHECKED, page);
        int countOfItems = serviceFactory.getFeedbackService().countFeedbacksByMasterId(requestedUserId);
        setPaginationAttrs(DEFAULT_SIZE, countOfItems);
    }
}