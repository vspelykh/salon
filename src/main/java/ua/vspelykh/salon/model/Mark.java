package ua.vspelykh.salon.model;

import java.time.LocalDateTime;

public class Mark extends AbstractBaseEntity{

    private Integer appointmentId;
    private int mark;
    private String comment;
    private LocalDateTime date;

    public Mark() {
    }

    public Mark(Integer id, Integer appointmentId, int mark, String comment, LocalDateTime date) {
        super(id);
        this.appointmentId = appointmentId;
        this.mark = mark;
        this.comment = comment;
        this.date = date;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
