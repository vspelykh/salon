package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENTS;
import static ua.vspelykh.salon.controller.command.CommandNames.ORDERS;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class represents the command to retrieve filtered appointments and display them on the Orders page.
 *
 * @version 1.0
 */
public class OrdersCommand extends Command {

    /**
     * Processes the request to retrieve filtered appointments and display them on the Orders page.
     * Overrides the process() method of the Command class.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            Integer masterId = getParameterInt(ID);
            LocalDate dateFrom = getParameterLocalDate(DATE_FROM);
            LocalDate dateTo = getParameterLocalDate(DATE_TO);
            AppointmentStatus status = getParameterElseNull(STATUS) != null
                    ? AppointmentStatus.valueOf(getParameterElseNull(STATUS)) : null;
            PaymentStatus paymentStatus = getParameterElseNull(PAYMENT_STATUS) != null
                    ? PaymentStatus.valueOf(getParameterElseNull(PAYMENT_STATUS)) : null;
            List<AppointmentDto> appointments = getServiceFactory().getAppointmentService().
                    getFiltered(masterId, dateFrom, dateTo, status, paymentStatus, getPageParameter(), getSizeParameter());

            setRequestAttribute(APPOINTMENTS, appointments);
            setCheckedAttrs(masterId, dateFrom, dateTo, status, paymentStatus);
            setPaginationParams(getPageParameter(), getSizeParameter(), getServiceFactory().getAppointmentService().
                    getCountOfAppointments(masterId, dateFrom, dateTo, status, paymentStatus));
            forward(ORDERS);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Sets the checked filter attributes in the request.
     *
     * @param masterId      the ID of the master to filter by.
     * @param dateFrom      the start date to filter by.
     * @param dateTo        the end date to filter by.
     * @param status        the appointment status to filter by.
     * @param paymentStatus the payment status to filter by.
     */
    private void setCheckedAttrs(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) {
        setRequestAttribute(ID + CHECKED, masterId);
        setRequestAttribute(DATE_FROM + CHECKED, dateFrom);
        setRequestAttribute(DATE_TO + CHECKED, dateTo);
        if (status != null) {
            setRequestAttribute(STATUS + CHECKED, status.name());
        }
        if (paymentStatus != null) {
            setRequestAttribute(PAYMENT_STATUS + CHECKED, paymentStatus.name());
        }
        setRequestAttribute(STATUS, AppointmentStatus.values());
    }

    /**
     * Sets the pagination parameters in the request.
     *
     * @param page         the current page number.
     * @param size         the number of items to display per page.
     * @param countOfItems the total number of items to display.
     */
    private void setPaginationParams(int page, int size, int countOfItems) {
        setRequestAttribute(PAGE + CHECKED, page);
        setRequestAttribute(SIZE + CHECKED, size);
        setPaginationAttrs(size, countOfItems);
    }

    /**
     * Sets the filter attributes in the request.
     *
     * @throws ServiceException if there is an error in the service.
     */
    private void setFilterAttributes() throws ServiceException {
        setRequestAttribute(SIZES, SIZE_LIST);
        setRequestAttribute(MASTERS, getServiceFactory().getUserService().findMasters(true));
    }
}
