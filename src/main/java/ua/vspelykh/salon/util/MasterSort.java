package ua.vspelykh.salon.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum MasterSort implements Serializable {

    NAME_ASC("name asc"), NAME_DESC("name DESC"), RATING_ASC("rating asc"), RATING_DESC("rating DESC"),
    FIRST_PRO("first experienced"), FIRST_YOUNG("first young");

    String text;

    MasterSort(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static List<MasterSort> list() {
        List<MasterSort> sorts = new ArrayList<>();
        Collections.addAll(sorts, values());
        return sorts;
    }
}
