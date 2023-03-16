package ua.vspelykh.salon.util;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ua.vspelykh.salon.model.dto.AppointmentDto;

import java.time.LocalTime;

/**
 * ScheduleItem is a class used to represent a single item in a schedule.
 * It contains information about the appointment, start and end times of the item, and additional info.
 *
 * @version 1.0
 */
@Getter
@Builder
@EqualsAndHashCode
public class ScheduleItem {

    private AppointmentDto appointment;
    private final LocalTime start;
    private final LocalTime end;
    private final String info;

    /**
     * Constructs a new ScheduleItem object with a specified start time, end time, and additional information
     * that used to create a schedule item that represents a free slot in the schedule or a weekend item.
     *
     * @param start The start time of the schedule item.
     * @param end   The end time of the schedule item.
     * @param info  Additional information about the schedule item.
     */
    public ScheduleItem(LocalTime start, LocalTime end, String info) {
        this.start = start;
        this.end = end;
        this.info = info;
    }

    /**
     * Constructs a new ScheduleItem object with a specified appointment, start time, end time, and additional information.
     *
     * @param appointment The appointment associated with this schedule item.
     * @param start       The start time of the schedule item.
     * @param end         The end time of the schedule item.
     * @param info        Additional information about the schedule item.
     */
    public ScheduleItem(AppointmentDto appointment, LocalTime start, LocalTime end, String info) {
        this.appointment = appointment;
        this.start = start;
        this.end = end;
        this.info = info;
    }

    @Override
    public String toString() {
        return start + " " + end + " " + info + "\n";
    }
}