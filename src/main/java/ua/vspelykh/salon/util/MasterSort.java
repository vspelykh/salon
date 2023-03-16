package ua.vspelykh.salon.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An enumeration of possible sorting options for the list of masters.
 *
 * @version 1.0
 */
public enum MasterSort implements Serializable {

    NAME_ASC, NAME_DESC, RATING_ASC, RATING_DESC, FIRST_PRO, FIRST_YOUNG;

    /**
     * Returns a list of all possible sorting options.
     *
     * @return a list of all possible sorting options
     */
    public static List<MasterSort> list() {
        List<MasterSort> sorts = new ArrayList<>();
        Collections.addAll(sorts, values());
        return sorts;
    }
}
