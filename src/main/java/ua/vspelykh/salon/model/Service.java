package ua.vspelykh.salon.model;

public class Service extends AbstractBaseEntity {

    private int masterId;
    private int baseServiceId;
    private int continuance;

    public Service() {
    }

    public Service(Integer id, int masterId, int baseServiceId, int continuance) {
        super(id);
        this.masterId = masterId;
        this.baseServiceId = baseServiceId;
        this.continuance = continuance;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getBaseServiceId() {
        return baseServiceId;
    }

    public void setBaseServiceId(int baseServiceId) {
        this.baseServiceId = baseServiceId;
    }

    public int getContinuance() {
        return continuance;
    }

    public void setContinuance(int continuance) {
        this.continuance = continuance;
    }
}
