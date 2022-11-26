package ua.vspelykh.salon.model;

public class MastersLevel extends AbstractBaseEntity {

    private String name;
    private double index;

    public MastersLevel() {
    }

    public MastersLevel(Integer id, String name, double index) {
        super(id);
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }
}
