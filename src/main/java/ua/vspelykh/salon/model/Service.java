package ua.vspelykh.salon.model;

public class Service extends AbstractBaseEntity{

    private User master;
    private BaseService baseService;
    private int continuance;

    public Service() {

    }

    public Service(Integer id, User master, BaseService baseService, int continuance) {
        super(id);
        this.master = master;
        this.baseService = baseService;
        this.continuance = continuance;
    }
}
