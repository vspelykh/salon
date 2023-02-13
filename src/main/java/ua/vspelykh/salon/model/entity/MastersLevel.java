package ua.vspelykh.salon.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum MastersLevel implements Serializable {

    YOUNG(1), TOP(1.15), PRO(1.3);

    private final double index;

    MastersLevel(double index) {
        this.index = index;
    }

    public double getIndex() {
        return index;
    }

    public String getName() {
        return this.name();
    }

    public static List<MastersLevel> list(){
        List<MastersLevel> levels = new ArrayList<>();
        Collections.addAll(levels, values());
        return levels;
    }
}
