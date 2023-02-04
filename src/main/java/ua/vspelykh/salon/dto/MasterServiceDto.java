package ua.vspelykh.salon.dto;

public class MasterServiceDto {

    private int id;
    private int masterId;
    private BaseServiceDto service;
    private int continuance;

    public MasterServiceDto(int id, int masterId, BaseServiceDto baseService, int continuance) {
        this.id = id;
        this.masterId = masterId;
        this.service = baseService;
        this.continuance = continuance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public BaseServiceDto getService() {
        return service;
    }

    public void setService(BaseServiceDto service) {
        this.service = service;
    }

    public int getContinuance() {
        return continuance;
    }

    public void setContinuance(int continuance) {
        this.continuance = continuance;
    }
}
