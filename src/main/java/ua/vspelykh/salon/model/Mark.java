package ua.vspelykh.salon.model;

import java.time.LocalDateTime;

public class Mark extends AbstractBaseEntity{

    private Appointment appointment;
    private int mark;
    private String comment;
    private LocalDateTime date;

    public Mark() {
    }

    public Mark(Integer id, Appointment appointment, int mark, String comment, LocalDateTime date) {
        super(id);
        this.appointment = appointment;
        this.mark = mark;
        this.comment = comment;
        this.date = date;
    }
}
