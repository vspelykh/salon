package ua.vspelykh.salon.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ScheduleBuilderException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.vspelykh.salon.controller.ControllerConstants.INTERVAL;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_500;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_SCHEDULE_BUILDER;

/**
 * This class is responsible for building a schedule based on a given list of appointments, working day, and locale.
 * <p>
 * If the working day is null, a weekend schedule is created. Otherwise, the builder adds the appointments to the
 * schedule and fills in free slots with ScheduleItems indicating they are available for booking.
 *
 * @version 1.0
 */
public class ScheduleBuilder {

    private static final Logger LOG = LogManager.getLogger(ScheduleBuilder.class);

    private final List<ScheduleItem> items = new ArrayList<>();
    private final List<AppointmentDto> appointments;
    private final WorkingDay day;
    private final String locale;
    private boolean isWeekend = false;
    private final Map<Integer, List<LocalTime>> freeSlotsForAppointments = new HashMap<>();

    private final ServiceFactory serviceFactory;

    private static final String FREE_SLOT = "Free slot";

    private static final LocalTime START_WORKING_DAY = LocalTime.of(8, 0);
    private static final LocalTime END_WORKING_DAY = LocalTime.of(20, 0);
    private static final String WEEKEND = "weekend";

    /**
     * Constructs a new ScheduleBuilder instance.
     *
     * @param appointments   the list of appointments to be scheduled
     * @param day            the working day for which to schedule appointments
     * @param locale         the locale in which to display the scheduled appointments
     * @param serviceFactory the service factory used to retrieve information about the services being scheduled
     * @throws IllegalArgumentException if any of the arguments are null
     */
    public ScheduleBuilder(List<AppointmentDto> appointments, WorkingDay day, String locale, ServiceFactory serviceFactory) {
        if (day == null) {
            items.add(getWeekend());
            isWeekend = true;
        }
        this.appointments = appointments;
        this.day = day;
        this.locale = locale;
        this.serviceFactory = serviceFactory;
    }

    /**
     * Builds a list of ScheduleItems based on the provided appointments and working day.
     *
     * @return a list of ScheduleItems representing the schedule
     * @throws ScheduleBuilderException if an error occurs while building the schedule
     */
    public List<ScheduleItem> build() throws ScheduleBuilderException {
        if (!isWeekend) {
            addItemsFromAppointments();
            addFreeSlots();
        }
        return getItems();
    }

    /**
     * Creates a ScheduleItem for a weekend day with default start and end time
     *
     * @return a ScheduleItem object representing a weekend day
     */
    private ScheduleItem getWeekend() {
        return new ScheduleItem(START_WORKING_DAY, END_WORKING_DAY, WEEKEND);
    }

    /**
     * Adds schedule items to the builder based on the given appointments.
     *
     * @throws ScheduleBuilderException if an error occurs while building the schedule
     */
    private void addItemsFromAppointments() throws ScheduleBuilderException {
        try {
            for (AppointmentDto appointment : appointments) {
                LocalTime start = appointment.getDate().toLocalTime();
                LocalTime end = start.plusMinutes(appointment.getContinuance());
                StringJoiner joiner = new StringJoiner(", ");
                for (AppointmentItem appointmentItem : appointment.getAppointmentItems()) {
                    joiner.add(getServiceName(appointmentItem));
                }
                items.add(new ScheduleItem(appointment, start, end, joiner.toString()));
                freeSlotsForAppointments.put(appointment.getId(), getPossibleSlotsForAppointment(day, appointment));
            }
        } catch (ServiceException e) {
            LOG.error(ERROR_SCHEDULE_BUILDER);
            throw new ScheduleBuilderException(ERROR_500);
        }
    }

    /**
     * Returns a list of possible time slots for a given appointment on a given working day.
     * <p>
     * The code first checks if the appointment's date is before today's date or if the appointment has already been
     * completed (i.e., has a status of SUCCESS). If either of these conditions is true, the method
     * returns an empty list, indicating that there are no possible time slots for this appointment.
     *
     * @param day         The working day for which the time slots are calculated.
     * @param appointment The appointment for which the time slots are calculated.
     * @return A list of possible time slots for the appointment.
     */
    private List<LocalTime> getPossibleSlotsForAppointment(WorkingDay day, AppointmentDto appointment) {
        if (appointment.getDate().toLocalDate().isBefore(LocalDate.now())
                || appointment.getStatus().equals(AppointmentStatus.SUCCESS)) {
            return Collections.emptyList();
        }
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlotsForDtos(slots, appointments, INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        List<LocalTime> possibleSlots = new ArrayList<>();
        calculatePossibleSlotsForAppointment(appointment, slots, possibleSlots);
        return possibleSlots;
    }

    /**
     * Calculates the possible time slots for a given appointment based on the available time slots for a working day.
     *
     * @param appointment   The appointment for which to calculate the possible time slots.
     * @param slots         The list of available time slots for the working day.
     * @param possibleSlots The list to store the possible time slots.
     */
    private void calculatePossibleSlotsForAppointment(AppointmentDto appointment, List<LocalTime> slots,
                                                      List<LocalTime> possibleSlots) {
        for (int i = 0; i < slots.size(); i++) {
            LocalTime current = slots.get(i);
            int count = 1;
            for (LocalTime another : slots) {
                while (current.plusMinutes((long) count * INTERVAL).equals(another)) {
                    ++count;
                }
                if (appointment.getContinuance() <= count * INTERVAL) {
                    possibleSlots.add(current);
                    break;
                }
            }
        }
    }

    /**
     * Adds free schedule items to the schedule for the time slots that are available on the working day
     * and not occupied by any existing appointments.
     * <p>
     * The method first gets a list of all possible time slots for the working day, and then removes any
     * time slots that are already occupied by existing appointments. It then iterates over the remaining
     * time slots and groups them into contiguous free time slots. For each group of contiguous free time slots,
     * a new ScheduleItem object is created and added to the list of items.
     */
    private void addFreeSlots() {
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlotsForDtos(slots, appointments, INTERVAL);
        LocalTime start = slots.get(0);
        int count = 0;
        for (int i = 0; i < slots.size() - 1; i++) {
            LocalTime currentSlot = slots.get(i);
            if (currentSlot.plusMinutes(INTERVAL).equals(slots.get(i + 1))) {
                count++;
            } else {
                addFreeScheduleItem(start, count);
                count = 0;
                start = slots.get(i + 1);
            }
        }
        addFreeScheduleItem(start, count);
    }

    /**
     * Adds free schedule items to the list of schedule items based on available time slots that were not occupied by
     * appointments. Free slots are created by grouping available slots that are adjacent to each other.
     *
     * @param start The start time of the first free slot.
     * @param count The number of consecutive available time slots starting from the start time.
     */
    private void addFreeScheduleItem(LocalTime start, int count) {
        LocalTime end = start.plusMinutes((long) INTERVAL * count);
        ScheduleItem item = new ScheduleItem(start, end.plusMinutes(INTERVAL), FREE_SLOT);
        items.add(item);
    }

    /**
     * Returns the name of a service based on the locale and the service ID contained within an AppointmentItem object.
     * <p>
     * The method checks the locale to determine whether to return the Ukrainian or English name of the service. It then
     * uses the serviceFactory to find the service based on the ID contained in the AppointmentItem. If the service is
     * found, the appropriate name is returned. If not, a ServiceException is thrown.
     *
     * @param appointmentItem The AppointmentItem object containing the service ID.
     * @return The name of the service in the appropriate locale.
     * @throws ServiceException if the service cannot be found.
     */
    private String getServiceName(AppointmentItem appointmentItem) throws ServiceException {
        return UA_LOCALE.equals(locale) ? serviceFactory.getBaseServiceService().findById(appointmentItem.getServiceId()).getServiceUa()
                : serviceFactory.getBaseServiceService().findById(appointmentItem.getServiceId()).getService();
    }

    public List<ScheduleItem> getItems() {
        return items.stream().sorted(Comparator.comparing(ScheduleItem::getStart)).collect(Collectors.toList());
    }

    public Map<Integer, List<LocalTime>> getFreeSlotsForAppointments() {
        return freeSlotsForAppointments;
    }
}
