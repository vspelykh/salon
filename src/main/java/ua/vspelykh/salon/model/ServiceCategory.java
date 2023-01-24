package ua.vspelykh.salon.model;

public class ServiceCategory extends AbstractBaseEntity {

    private String name;

    public ServiceCategory() {
    }

    public ServiceCategory(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
