package ua.vspelykh.salon.model;

public class Ordering extends AbstractBaseEntity {

    private Integer appointmentId;
    private Integer serviceId;

    public Ordering() {
    }

    public Ordering(Integer id, Integer appointmentId, Integer serviceId) {
        super(id);
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}
