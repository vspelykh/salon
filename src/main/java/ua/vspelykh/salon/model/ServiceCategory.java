package ua.vspelykh.salon.model;

public class ServiceCategory extends AbstractBaseEntity {

    private String name;
    private String nameUa;

    public ServiceCategory() {
    }

    public ServiceCategory(Integer id, String name, String nameUa) {
        super(id);
        this.name = name;
        this.nameUa = nameUa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUa() {
        return nameUa;
    }
}
