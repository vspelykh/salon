package ua.vspelykh.salon.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The MastersLevel enum represents the different levels of mastery for a service provider in the application.
 * Each level has an associated index, which is used to calculate the price of a service based on the master's level.
 * <p>
 * The available levels are:
 * - YOUNG: A young or junior master with the lowest index coefficient.
 * - PRO: A professional master with a higher index coefficient than YOUNG.
 * - TOP: A top-level master with the highest index coefficient.
 * <p>
 * Use the getIndex() method to get the index coefficient for a particular level.
 * Use the getName() method to get the name of a particular level.
 * Use the list() method to get a list of all available levels.
 *
 * @version 1.0
 */
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

    /**
     * This method is a utility method for retrieving a list of all possible values of the MastersLevel enum.
     *
     * @return a new ArrayList with all the values of the enum.
     */
    public static List<MastersLevel> list() {
        List<MastersLevel> levels = new ArrayList<>();
        Collections.addAll(levels, values());
        return levels;
    }
}
