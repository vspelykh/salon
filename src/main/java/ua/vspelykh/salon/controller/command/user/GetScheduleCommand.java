package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.ScheduleBuilder;
import ua.vspelykh.salon.util.ScheduleItem;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.STATUS;
import static ua.vspelykh.salon.util.SalonUtils.getLocalDate;

public class GetScheduleCommand extends AbstractScheduleCommand {

    @Override
    public void process() throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter(ID));
            String[] datesArray = request.getParameter(DAYS).split(", ");
            Map<LocalDate, List<ScheduleItem>> mapOfSchedules = new TreeMap<>(Comparator.naturalOrder());
            Map<Integer, List<LocalTime>> freeSlots = new LinkedHashMap<>();
            String locale = (String) request.getSession().getAttribute(LANG);

            buildSchedule(userId, datesArray, mapOfSchedules, freeSlots, locale);
            setAttrs(userId, mapOfSchedules, freeSlots);
            forward(GET_SCHEDULE);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void buildSchedule(int userId, String[] datesArray, Map<LocalDate, List<ScheduleItem>> mapOfSchedules, Map<Integer, List<LocalTime>> freeSlots, String locale) throws ServiceException {
        for (String date : datesArray) {
            List<AppointmentDto> appointments =
                    getServiceFactory().getAppointmentService().getDTOsByDateAndMasterId(getLocalDate(date), userId);
            WorkingDay day = getServiceFactory().getWorkingDayService().getDayByUserIdAndDate(userId, getLocalDate(date));
            ScheduleBuilder builder = new ScheduleBuilder(appointments, day, locale, serviceFactory);
            List<ScheduleItem> schedule = builder.build();
            mapOfSchedules.put(getLocalDate(date), schedule);
            freeSlots.putAll(builder.getFreeSlotsForAppointments());
        }
    }

    private void setAttrs(int userId, Map<LocalDate, List<ScheduleItem>> mapOfSchedules, Map<Integer, List<LocalTime>> freeSlots) throws ServiceException {
        request.setAttribute(FREE_SLOTS_MAP, freeSlots);
        request.setAttribute(STATUS, AppointmentStatus.values());
        request.setAttribute(SCHEDULE, mapOfSchedules);
        request.setAttribute(USER, serviceFactory.getUserService().findById(userId));
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        request.setAttribute(IS_ADMIN, currentUser.getRoles().contains(Role.ADMINISTRATOR));
    }
}
