package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Feedback extends AbstractBaseEntity{

    private Integer appointmentId;
    private int mark;
    private String comment;
    private LocalDateTime date;

    public Feedback(Integer appointmentId, int mark, String comment, LocalDateTime date) {
        this.appointmentId = appointmentId;
        this.mark = mark;
        this.comment = comment;
        this.date = date;
    }

    public Feedback(Integer id, Integer appointmentId, int mark, String comment, LocalDateTime date) {
        super(id);
        this.appointmentId = appointmentId;
        this.mark = mark;
        this.comment = comment;
        this.date = date;
    }

}
