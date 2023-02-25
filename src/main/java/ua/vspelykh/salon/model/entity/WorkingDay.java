package ua.vspelykh.salon.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(of = {"date"})
public class WorkingDay extends AbstractBaseEntity {

    private Integer userId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public WorkingDay(Integer userId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.userId = userId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public WorkingDay(Integer id, Integer userId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        super(id);
        this.userId = userId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
}
