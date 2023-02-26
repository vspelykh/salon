package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.*;
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
import static ua.vspelykh.salon.model.dao.mapper.Column.MASTER_ID;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;

public class CreateAppointmentCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            List<MasterService> masterServices = parseServices();
            validateAndSaveAppointment(masterServices);
            request.getSession().setAttribute(MESSAGE, APPOINTMENT + DOT + SUCCESS);
            redirect(SUCCESS_REDIRECT);
        } catch (ServiceException e) {
            setErrorMessageAndRedirectToCalendarPage();
        }
    }

    private List<MasterService> parseServices() throws ServiceException {
        List<MasterService> masterServices = new ArrayList<>();
        if (checkNullParam(SERVICES)) {
            for (String service : request.getParameterValues(SERVICES)) {
                int serviceId = Integer.parseInt(service.split("[|]")[3]);
                masterServices.add(getServiceFactory().getServiceService().findById(serviceId));
            }
        }
        return masterServices;
    }

    private void validateAndSaveAppointment(List<MasterService> masterServices) throws ServiceException {
        User master = getServiceFactory().getUserService().findById(Integer.valueOf(request.getParameter(MASTER_ID)));
        User client = (User) request.getSession().getAttribute(CURRENT_USER);
        LocalDate date = SalonUtils.getLocalDate(request.getParameter(DAY));
        LocalTime time = LocalTime.parse(request.getParameter(TIME));
        PaymentStatus paymentStatus = PaymentStatus.valueOf(request.getParameter(PAYMENT));
        Appointment appointment = buildAppointment(masterServices, master, client, date, time, paymentStatus);
        WorkingDay day = getServiceFactory().getWorkingDayService().getDayByUserIdAndDate(master.getId(), date);
        List<Appointment> appointments = getServiceFactory().getAppointmentService().getByDateAndMasterId(date, master.getId());
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId()), INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        int allowedMinutes = countAllowedMinutes(getTime(String.valueOf(time)), appointments, day);
        if (!slots.contains(time) || allowedMinutes < appointment.getContinuance()) {
            throw new ServiceException("Time slot have already occupied or duration not allowed anymore.");
        }
        getServiceFactory().getAppointmentService().save(appointment, masterServices);
    }

    private Appointment buildAppointment(List<MasterService> masterServices, User master, User client, LocalDate date, LocalTime time, PaymentStatus paymentStatus) throws ServiceException {
        return Appointment.builder()
                .masterId(master.getId())
                .clientId(client.getId())
                .continuance(getTotalContinuance(masterServices))
                .date(LocalDateTime.of(date, time))
                .price(getTotalPrice(masterServices, getServiceFactory().getUserService().getUserLevelByUserId(master.getId())))
                .discount(1)
                .paymentStatus(paymentStatus)
                .status(AppointmentStatus.RESERVED).build();
    }

    private int getTotalPrice(List<MasterService> masterServices, UserLevel userLevel) throws ServiceException {
        double totalPrice = 0;

        for (MasterService masterService : masterServices) {
            BaseService baseService = getServiceFactory().getBaseServiceService().findById(masterService.getBaseServiceId());
            totalPrice += baseService.getPrice() * userLevel.getLevel().getIndex();
        }
        return (int) totalPrice;
    }

    private int getTotalContinuance(List<MasterService> masterServices) {
        int totalContinuance = 0;
        for (MasterService masterService : masterServices) {
            totalContinuance += masterService.getContinuance();
        }
        return totalContinuance;
    }

    private void setErrorMessageAndRedirectToCalendarPage() throws IOException {
        request.getSession().setAttribute(ERROR, HAS_ERROR);
        redirect(request.getContextPath() + HOME_REDIRECT + "?command=calendar&day=" + request.getParameter(DAY)
                + "&id=" + request.getParameter(MASTER_ID));
    }
}
