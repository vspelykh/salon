package ua.vspelykh.salon.util;

import java.io.Serializable;
import java.time.LocalTime;

public class ScheduleItem implements Serializable {

    private LocalTime start;
    private LocalTime end;
    private String info;

    public ScheduleItem(LocalTime start, LocalTime end, String info) {
        this.start = start;
        this.end = end;
        this.info = info;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return start + " " + end + " " + info + "\n";
    }
}