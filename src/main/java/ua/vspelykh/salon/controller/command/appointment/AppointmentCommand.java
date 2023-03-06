package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.discount.DiscountHandler;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.model.dao.mapper.Column.DISCOUNT;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.TimeSlotsUtils.countAllowedMinutes;

/**
 * The AppointmentCommand class is a concrete implementation of the Command pattern, responsible for handling
 * requests related to booking an appointment with a specific master user.
 * It processes the request parameters and interacts with the application services to retrieve and process the necessary
 * data. If an error occurs during the processing of the request, it sends a 404 error response to the client.
 *
 * @version 1.0
 */
public class AppointmentCommand extends Command {

    /**
     * Processes the request parameters to retrieve the user ID of the master user, and retrieves the necessary data
     * from the application services to generate the appointment booking page for the specified master user.
     * If an error occurs during the processing of the request, it sends a 404 error response to the client.
     *
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            int masterId = getParameterInt(ID);
            User master = serviceFactory.getUserService().findById(masterId);
            int allowedTime = getAllowedTime(master);
            if (allowedTime <= 0) {
                sendError404();
                return;
            }
            setAttributes(master, allowedTime);
            setDiscountAttribute();
            forward(APPOINTMENT);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Retrieves the number of allowed minutes for an appointment booking with the specified master user, based on
     * the requested date and time, and the existing appointments and working hours of the master user.
     *
     * @param master the master user for whom the appointment is being booked.
     * @return the number of allowed minutes for an appointment booking with the specified master user.
     * @throws ServiceException if an error occurs while retrieving the necessary data from the application services.
     */
    private int getAllowedTime(User master) throws ServiceException {
        LocalDate day = getParameterLocalDate(DAY);
        List<Appointment> appointments = serviceFactory.getAppointmentService().getByDateAndMasterId(day, master.getId());
        Time time = getParameterTime(TIME);
        return countAllowedMinutes(time, appointments, serviceFactory.getWorkingDayService().getByUserIdAndDate(master.getId(), day));
    }

    /**
     * Sets the necessary request attributes for the appointment booking page, including the master user, the
     * requested date and time, the list of available services, and the number of allowed minutes for an appointment
     * booking with the specified master user.
     *
     * @param master      the master user for whom the appointment is being booked.
     * @param allowedTime the number of allowed minutes for an appointment booking with the specified master user.
     * @throws ServiceException if an error occurs while retrieving the necessary data from the application services.
     */
    private void setAttributes(User master, int allowedTime) throws ServiceException {
        setRequestAttribute(MASTER, master);
        setRequestAttribute(ID, getParameter(ID));
        setRequestAttribute(DAY, getParameter(DAY));
        setRequestAttribute(TIME, getParameter(TIME));
        List<MasterServiceDto> dtos = serviceFactory.getServiceService().getDTOsByMasterId(master.getId(), getLocale());
        setRequestAttribute(SERVICES, dtos);
        setRequestAttribute(FIRST, dtos.get(0).getId());
        setRequestAttribute(SIZE, dtos.size());
        setRequestAttribute(USER_LEVEL, serviceFactory.getUserService().getUserLevelByUserId(master.getId()));
        setRequestAttribute(ALLOWED_TIME, allowedTime);
    }

    /**
     * Sets the discount attribute by calculating the discount percentage using the current user and the
     * specified date and time.
     */
    private void setDiscountAttribute() {
        LocalDateTime dateTime = LocalDateTime.of(getParameterLocalDate(DAY), getParameterLocalTime(TIME));
        DiscountHandler handler = new DiscountHandler(getCurrentUser(), dateTime);
        setRequestAttribute(DISCOUNT, handler.getDiscountPercentage());
    }
}
