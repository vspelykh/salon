package ua.vspelykh.salon.model;

import java.io.Serializable;

public class UserLevel implements Serializable {

    private int masterId;
    private MastersLevel level;

    public UserLevel() {
    }

    public UserLevel(int masterId, MastersLevel level) {
        this.masterId = masterId;
        this.level = level;
    }

    public int getMasterId() {
        return masterId;
    }

    public MastersLevel getLevel() {
        return level;
    }

    public void setLevel(MastersLevel level) {
        this.level = level;
    }
}
