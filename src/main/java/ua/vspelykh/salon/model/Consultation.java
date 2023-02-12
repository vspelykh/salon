package ua.vspelykh.salon.model;

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
public class Consultation extends AbstractBaseEntity {

    private String name;
    private String number;
    private LocalDateTime date;

    public Consultation(Integer id, String name, String number) {
        super(id);
        this.name = name;
        this.number = number;
    }

    public Consultation(Integer id, String name, String number, LocalDateTime date) {
        super(id);
        this.name = name;
        this.number = number;
        this.date = date;
    }
}
