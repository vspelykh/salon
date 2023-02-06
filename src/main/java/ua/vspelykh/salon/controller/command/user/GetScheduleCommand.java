package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.util.ScheduleBuilder;
import ua.vspelykh.salon.util.ScheduleItem;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.dao.mapper.Column.ID;
import static ua.vspelykh.salon.dao.mapper.Column.STATUS;
import static ua.vspelykh.salon.util.SalonUtils.getLocalDate;

public class GetScheduleCommand extends AbstractScheduleCommand {

    private static final String FREE_SLOTS_MAP = "free_slots_map";

    @Override
    public void process() throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter(ID));
            String[] datesArray = request.getParameter(DAYS).split(", ");
            Map<LocalDate, List<ScheduleItem>> mapOfSchedules = new TreeMap<>(Comparator.naturalOrder());
            Map<Integer, List<LocalTime>> freeSlots = new LinkedHashMap<>();
            String locale = (String) request.getSession().getAttribute(LANG);

            scheduleBuildingProcess(userId, datesArray, mapOfSchedules, freeSlots, locale);
            setAttrs(userId, mapOfSchedules, freeSlots);

            forward(GET_SCHEDULE);
        } catch (ServiceException e) {
            e.printStackTrace();
            //TODO
        }
    }

    private void scheduleBuildingProcess(int userId, String[] datesArray, Map<LocalDate, List<ScheduleItem>> mapOfSchedules, Map<Integer, List<LocalTime>> freeSlots, String locale) throws ServiceException {
        for (String date : datesArray) {
            List<AppointmentDto> appointments =
                    getServiceFactory().getAppointmentService().getDtosByDateAndMasterId(getLocalDate(date), userId);
            WorkingDay day = getServiceFactory().getWorkingDayService().getDayByUserIdAndDate(userId, getLocalDate(date));
            ScheduleBuilder builder = new ScheduleBuilder(appointments, day, locale, serviceFactory);
            List<ScheduleItem> schedule = builder.build();
            mapOfSchedules.put(getLocalDate(date), schedule);
            freeSlots.putAll(builder.getFreeSlotsForAppointments());
        }
    }

    @SuppressWarnings("unchecked")
    private void setAttrs(int userId, Map<LocalDate, List<ScheduleItem>> mapOfSchedules, Map<Integer, List<LocalTime>> freeSlots) throws ServiceException {
        request.setAttribute(FREE_SLOTS_MAP, freeSlots);
        request.setAttribute(STATUS, AppointmentStatus.values());
        request.setAttribute(SCHEDULE, mapOfSchedules);
        request.setAttribute(USER, serviceFactory.getUserService().findById(userId));
        Set<Role> roles = (Set<Role>) request.getSession().getAttribute("roles");
        request.setAttribute(IS_ADMIN, roles.contains(Role.ADMINISTRATOR));
    }
}