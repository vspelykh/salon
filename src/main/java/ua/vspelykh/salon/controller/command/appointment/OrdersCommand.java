package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
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
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            AppointmentFilter filter = buildAppointmentFilter();
            List<AppointmentDto> appointments = getServiceFactory().getAppointmentService().
                    getFiltered(filter, getPageParameter(), getSizeParameter());

            setRequestAttribute(APPOINTMENTS, appointments);
            setCheckedAttrs(filter);
            setPaginationParams(getPageParameter(), getSizeParameter(), getServiceFactory().getAppointmentService().
                    getCountOfAppointments(filter));
            forward(ORDERS);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Builds an AppointmentFilter object based on the query parameters of the current HTTP request.
     *
     * @return an AppointmentFilter object that contains the filter criteria for appointment
     */
    private AppointmentFilter buildAppointmentFilter() {
        AppointmentStatus status = getParameterElseNull(STATUS) != null
                ? AppointmentStatus.valueOf(getParameterElseNull(STATUS)) : null;
        PaymentStatus paymentStatus = getParameterElseNull(PAYMENT_STATUS) != null
                ? PaymentStatus.valueOf(getParameterElseNull(PAYMENT_STATUS)) : null;
        return AppointmentFilter.builder()
                .masterId(getParameterInt(ID))
                .dateFrom(getParameterLocalDate(DATE_FROM))
                .dateTo(getParameterLocalDate(DATE_TO))
                .status(status)
                .paymentStatus(paymentStatus)
                .build();
    }

    /**
     * This method sets the checked filter attributes in the current request based on the provided AppointmentFilter object.
     * The attributes are set with a CHECKED suffix to indicate that they should be pre-selected in the filter UI.
     *
     * @param filter the AppointmentFilter object containing the filter parameters to set in the request
     */
    private void setCheckedAttrs(AppointmentFilter filter) {
        setRequestAttribute(ID + CHECKED, filter.getMasterId());
        setRequestAttribute(DATE_FROM + CHECKED, filter.getDateFrom());
        setRequestAttribute(DATE_TO + CHECKED, filter.getDateTo());
        if (filter.getStatus() != null) {
            setRequestAttribute(STATUS + CHECKED, filter.getStatus().name());
        }
        if (filter.getPaymentStatus() != null) {
            setRequestAttribute(PAYMENT_STATUS + CHECKED, filter.getPaymentStatus().name());
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
