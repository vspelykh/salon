package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.model.PaymentStatus;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENTS;
import static ua.vspelykh.salon.controller.command.CommandNames.ORDERS;
import static ua.vspelykh.salon.dao.mapper.Column.*;

public class OrdersCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            Integer masterId = checkAndGetIntegerParam(ID);
            LocalDate dateFrom = checkAndGetLocalDateParam(DATE + "From");
            LocalDate dateTo = checkAndGetLocalDateParam(DATE + "To");
            AppointmentStatus status = request.getParameter(STATUS) != null && !request.getParameter(STATUS).isEmpty() ?
                    AppointmentStatus.valueOf(request.getParameter(STATUS)) : null;
            PaymentStatus paymentStatus = request.getParameter(PAYMENT_STATUS) != null && !request.getParameter(PAYMENT_STATUS)
                    .isEmpty() ? PaymentStatus.valueOf(request.getParameter(PAYMENT_STATUS)) : null;
            int page = request.getParameter(PAGE) == null ? 1 : Integer.parseInt(request.getParameter(PAGE));
            int size = request.getParameter(SIZE) == null ? 5 : Integer.parseInt(request.getParameter(SIZE));
            List<AppointmentDto> appointments = getServiceFactory().getAppointmentService().
                    getFiltered(masterId, dateFrom, dateTo, status, paymentStatus, page, size);
            request.setAttribute(APPOINTMENTS, appointments);
            setCheckedAttrs(masterId, dateFrom, dateTo, status, paymentStatus);
            setPaginationParams(page, size, getServiceFactory().getAppointmentService().
                    getCountOfAppointments(masterId, dateFrom, dateTo, status, paymentStatus));
            forward(ORDERS);
        } catch (ServiceException e) {
            response.sendError(404);
        }
    }

    private void setCheckedAttrs(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) {
        request.setAttribute(ID + CHECKED, masterId);
        request.setAttribute(DATE + "From" + CHECKED, dateFrom);
        request.setAttribute(DATE + "To" + CHECKED, dateTo);
        if (status != null) {
            request.setAttribute(STATUS + CHECKED, status.name());
        }
        if (paymentStatus != null){
            request.setAttribute(PAYMENT_STATUS + CHECKED, paymentStatus.name());
        }
        request.setAttribute(STATUS, AppointmentStatus.values());
    }

    private void setPaginationParams(int page, int size, int countOfItems) {
        request.setAttribute(PAGE + CHECKED, page);
        request.setAttribute(SIZE + CHECKED, size);
        countAndSet(size, countOfItems);
    }

    private void setFilterAttributes() throws ServiceException {
        request.setAttribute(SIZES, SIZE_ARRAY);
        request.setAttribute(MASTERS, getServiceFactory().getUserService().findMasters(true));
    }
}
