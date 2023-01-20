package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.BaseService;

public class MasterServiceDto {

    private int id;
    private int masterId;
    private BaseService service;
    private int continuance;

    public MasterServiceDto(int id, int masterId, BaseService baseService, int continuance) {
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

    public BaseService getService() {
        return service;
    }

    public void setService(BaseService service) {
        this.service = service;
    }

    public int getContinuance() {
        return continuance;
    }

    public void setContinuance(int continuance) {
        this.continuance = continuance;
    }
}
