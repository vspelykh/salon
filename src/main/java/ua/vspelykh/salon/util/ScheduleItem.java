package ua.vspelykh.salon.util;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ua.vspelykh.salon.model.dto.AppointmentDto;

import java.time.LocalTime;

@Getter
@Builder
@EqualsAndHashCode
public class ScheduleItem {

    private AppointmentDto appointment;
    private final LocalTime start;
    private final LocalTime end;
    private final String info;

    public ScheduleItem(LocalTime start, LocalTime end, String info) {
        this.start = start;
        this.end = end;
        this.info = info;
    }

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