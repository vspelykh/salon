package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.*;
import ua.vspelykh.salon.service.*;
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
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.DAY;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.TIME;
import static ua.vspelykh.salon.dao.mapper.Column.MASTER_ID;

public class CreateAppointmentCommand extends Command {

    private AppointmentService appointmentService = ServiceFactory.getAppointmentService();
    private UserService userService = ServiceFactory.getUserService();
    private ServiceService serviceService = ServiceFactory.getServiceService();
    private BaseServiceService bss = ServiceFactory.getBaseServiceService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            List<Service> services = new ArrayList<>();
            if (checkNullParam(SERVICES)) {
                for (String service : request.getParameterValues(SERVICES)) {
                    int serviceId = Integer.parseInt(service.split("[|]")[3]);
                    services.add(serviceService.findById(serviceId));
                }
            }
            User master = userService.findById(Integer.valueOf(request.getParameter(MASTER_ID)));
            User client = (User) request.getSession().getAttribute(CURRENT_USER);
            LocalDate date = SalonUtils.getLocaleDate(request.getParameter(DAY));
            LocalTime time = LocalTime.parse(request.getParameter(TIME));
            Appointment appointment = new Appointment(null, master.getId(), client.getId(),
                    getTotalContinuance(services), LocalDateTime.of(date, time),
                    getTotalPrice(services, userService.getUserLevelByUserId(master.getId())), 1);
            appointmentService.save(appointment);
            forward(HOME_PAGE);
        } catch (ServiceException e) {
            redirect(request.getContextPath() + HOME_REDIRECT + "?command=calendar&day=" + request.getParameter(DAY)
                    + "&id=" + request.getParameter(MASTER_ID) + "&exc=y");
        }
    }

    private int getTotalPrice(List<Service> services, UserLevel userLevel) throws ServiceException {
        double totalPrice = 0;

        for (Service service : services) {
            BaseService baseService = bss.findById(service.getBaseServiceId());
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
}
