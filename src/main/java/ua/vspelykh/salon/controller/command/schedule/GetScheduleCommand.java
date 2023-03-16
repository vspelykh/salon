package ua.vspelykh.salon.controller.command.schedule;

import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.ScheduleBuilder;
import ua.vspelykh.salon.util.ScheduleItem;
import ua.vspelykh.salon.util.exception.ScheduleBuilderException;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.STATUS;
import static ua.vspelykh.salon.util.SalonUtils.getLocalDate;


/**
 * GetScheduleCommand is a concrete class that extends the AbstractScheduleCommand class, and it is responsible
 * for processing the request to get the schedule for a particular user for a specified range of dates.
 *
 * @version 1.0
 */
public class GetScheduleCommand extends AbstractScheduleCommand {

    /**
     * Processes the request to get the schedule for a particular user for a specified range of dates.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            int userId = getParameterInt(ID);
            String[] datesArray = getParameter(DAYS).split(", ");
            Map<LocalDate, List<ScheduleItem>> mapOfSchedules = new TreeMap<>(Comparator.naturalOrder());
            Map<Integer, List<LocalTime>> freeSlots = new LinkedHashMap<>();

            buildSchedule(userId, datesArray, mapOfSchedules, freeSlots, getLocale());
            setAttrs(userId, mapOfSchedules, freeSlots);
            forward(GET_SCHEDULE);
        } catch (ServiceException | ScheduleBuilderException e) {
            sendError500();
        }
    }

    /**
     * Builds the schedule for each date in the given dates array by retrieving the list of appointments and
     * working day details from the service layer and building the schedule using ScheduleBuilder.
     * It also retrieves the free slots for each appointment and stores them in the free slots map.
     *
     * @param userId         the ID of the user for whom the schedule is to be built
     * @param datesArray     the array of dates for which the schedule is to be built
     * @param mapOfSchedules the map that holds the schedule for each date
     * @param freeSlots      the map that holds the free slots for each appointment
     * @param locale         the locale in which the schedule is to be built
     * @throws ServiceException if there is an error while retrieving data from the service layer
     */
    private void buildSchedule(int userId, String[] datesArray, Map<LocalDate, List<ScheduleItem>> mapOfSchedules,
                               Map<Integer, List<LocalTime>> freeSlots, String locale) throws ServiceException, ScheduleBuilderException {
        for (String date : datesArray) {
            List<AppointmentDto> appointments =
                    getServiceFactory().getAppointmentService().getDTOsByDateAndMasterId(getLocalDate(date), userId);

            WorkingDay day = getServiceFactory().getWorkingDayService().getByUserIdAndDate(userId, getLocalDate(date));
            ScheduleBuilder builder = new ScheduleBuilder(appointments, day, locale, serviceFactory);
            List<ScheduleItem> schedule = builder.build();
            mapOfSchedules.put(getLocalDate(date), schedule);
            freeSlots.putAll(builder.getFreeSlotsForAppointments());
        }
    }

    /**
     * Adds the specified schedule, list of free time slots, and other relevant attributes to the request attributes.
     *
     * @param userId         the ID of the user whose schedule was retrieved
     * @param mapOfSchedules a map containing the resulting schedule
     * @param freeSlots      the map that holds the free slots for each appointment
     * @throws ServiceException if there is an error while retrieving data from the service layer
     */
    private void setAttrs(int userId, Map<LocalDate, List<ScheduleItem>> mapOfSchedules, Map<Integer, List<LocalTime>> freeSlots) throws ServiceException {
        setRequestAttribute(FREE_SLOTS_MAP, freeSlots);
        setRequestAttribute(STATUS, AppointmentStatus.values());
        setRequestAttribute(SCHEDULE, mapOfSchedules);
        setRequestAttribute(USER, serviceFactory.getUserService().findById(userId));
        setRequestAttribute(IS_ADMIN, getCurrentUser().getRoles().contains(Role.ADMINISTRATOR));
    }
}
