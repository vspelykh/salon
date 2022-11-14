package ua.vspelykh.salon.model;

public class Ordering extends AbstractBaseEntity{

    private Appointment appointment;
    private Service service;

    public Ordering() {
    }

    public Ordering(Integer id, Appointment appointment, Service service) {
        super(id);
        this.appointment = appointment;
        this.service = service;
    }
}
