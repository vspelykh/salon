package ua.vspelykh.salon.model;

import java.time.LocalDateTime;

public class Consultation extends AbstractBaseEntity {

    private String name;
    private String number;
    private LocalDateTime date;

    public Consultation() {

    }

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

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
