package ua.vspelykh.salon.model;

import java.io.Serializable;

public enum MastersLevel implements Serializable {

    YOUNG(1), TOP(1.15), PRO(1.3);

    private final double index;

    MastersLevel(double index) {
        this.index = index;
    }

    public double getIndex() {
        return index;
    }
}
