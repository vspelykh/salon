package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.*;
import ua.vspelykh.salon.util.SalonUtils;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.*;
import static ua.vspelykh.salon.dao.mapper.Column.MASTER_ID;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;

public class CreateAppointmentCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            List<Service> services = new ArrayList<>();
            parsingServicesProcess(services);
            validateAndSaveAppointment(services);
            request.getSession().setAttribute(MESSAGE, APPOINTMENT + DOT +  SUCCESS);
            redirect(SUCCESS_REDIRECT);
        } catch (ServiceException e) {
            setErrorMessageAndRedirectToCalendarPage();
        }
    }

    private void parsingServicesProcess(List<Service> services) throws ServiceException {
        if (checkNullParam(SERVICES)) {
            for (String service : request.getParameterValues(SERVICES)) {
                int serviceId = Integer.parseInt(service.split("[|]")[3]);
                services.add(getServiceFactory().getServiceService().findById(serviceId));
            }
        }
    }

    private void validateAndSaveAppointment(List<Service> services) throws ServiceException {
        User master = getServiceFactory().getUserService().findById(Integer.valueOf(request.getParameter(MASTER_ID)));
        User client = (User) request.getSession().getAttribute(CURRENT_USER);
        LocalDate date = SalonUtils.getLocalDate(request.getParameter(DAY));
        LocalTime time = LocalTime.parse(request.getParameter(TIME));
        PaymentStatus paymentStatus = PaymentStatus.valueOf(request.getParameter(PAYMENT));
        Appointment appointment = Appointment.createAppointment(master.getId(), client.getId(), getTotalContinuance(services),
                LocalDateTime.of(date, time), getTotalPrice(services, getServiceFactory().getUserService().getUserLevelByUserId(master.getId())),
                1, paymentStatus);

        WorkingDay day = getServiceFactory().getWorkingDayService().getDayByUserIdAndDate(master.getId(), date);
        List<Appointment> appointments = getServiceFactory().getAppointmentService().getByDateAndMasterId(date, master.getId());
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId()), INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        int allowedMinutes = countAllowedMinutes(getTime(String.valueOf(time)), appointments, day);
        if (!slots.contains(time) || allowedMinutes < appointment.getContinuance()){
            throw new ServiceException("Time slot have already occupied or duration not allowed anymore.");
        }

        getServiceFactory().getAppointmentService().save(appointment);
    }

    private int getTotalPrice(List<Service> services, UserLevel userLevel) throws ServiceException {
        double totalPrice = 0;

        for (Service service : services) {
            BaseService baseService = getServiceFactory().getBaseServiceService().findById(service.getBaseServiceId());
            totalPrice += baseService.getPrice() * userLevel.getLevel().getIndex();
        }
        return (int) totalPrice;
    }

    private int getTotalContinuance(List<Service> services) {
        int totalContinuance = 0;
        for (Service service : services) {
            totalContinuance += service.getContinuance();
        }
        return totalContinuance;
    }

    private void setErrorMessageAndRedirectToCalendarPage() throws ServletException, IOException {
        request.getSession().setAttribute(ERROR, HAS_ERROR);
        redirect(request.getContextPath() + HOME_REDIRECT + "?command=calendar&day=" + request.getParameter(DAY)
                + "&id=" + request.getParameter(MASTER_ID));
    }
}
