package ua.vspelykh.salon.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkingDay extends AbstractBaseEntity{

    private Integer userId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public WorkingDay() {
    }

    public WorkingDay(Integer id, Integer userId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        super(id);
        this.userId = userId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}
