package ua.vspelykh.salon.util;

import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.INTERVAL;

public class ScheduleBuilder {

    private static final String FREE_SLOT = "Free slot";

    private List<ScheduleItem> items = new ArrayList<>();
    private final List<AppointmentDto> appointments;
    private final WorkingDay day;
    private String locale;

    private BaseServiceService baseService = ServiceFactory.getBaseServiceService();

    public ScheduleBuilder(List<AppointmentDto> appointments, WorkingDay day, String locale) {
        this.appointments = appointments;
        this.day = day;
        this.locale = locale;
    }

    public List<ScheduleItem> build() {
        addItemsFromAppointments();
        addFreeSlots();
        return getItems();
    }

    private void addItemsFromAppointments() {
        try {
            for (AppointmentDto appointment : appointments) {
                LocalTime start = appointment.getDate().toLocalTime();
                LocalTime end = start.plusMinutes(appointment.getContinuance());
                StringJoiner joiner = new StringJoiner(", ");
                for (Ordering ordering : appointment.getOrderings()) {
                    joiner.add(getServiceName(ordering));
                }
                items.add(new ScheduleItem(start, end, joiner.toString()));
            }
        } catch (ServiceException e) {
            //TODO
            e.printStackTrace();
        }
    }

    private void addFreeSlots(){
        List<LocalTime> slots = TimeSlotsUtils.getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        TimeSlotsUtils.removeOccupiedSlotsForDtos(slots, appointments, INTERVAL);
        LocalTime start = slots.get(0);
        int count = 0;
        for (int i = 0; i < slots.size()-1; i++){
            LocalTime currentSlot = slots.get(i);
            if (currentSlot.plusMinutes(INTERVAL).equals(slots.get(i + 1))){
                count++;
            } else {
                addFreeScheduleItem(start, count);
                count = 0;
                start = slots.get(i + 1);
            }
        }
        addFreeScheduleItem(start, count);
    }

    private void addFreeScheduleItem(LocalTime start, int count) {
        LocalTime end = start.plusMinutes((long) INTERVAL * count);
        ScheduleItem item = new ScheduleItem(start, end.plusMinutes(INTERVAL), FREE_SLOT);
        items.add(item);
    }

    private String getServiceName(Ordering ordering) throws ServiceException {
        return "ua".equals(locale) ? baseService.findById(ordering.getServiceId()).getServiceUa()
                : baseService.findById(ordering.getServiceId()).getService();
    }

    public List<ScheduleItem> getItems() {
        return items.stream().sorted(Comparator.comparing(ScheduleItem::getStart)).collect(Collectors.toList());
    }

}
