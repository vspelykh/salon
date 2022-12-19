package ua.vspelykh.salon.model;

import java.io.Serializable;

public class UserLevel implements Serializable {

    private int masterId;
    private MastersLevel level;
    private boolean isActive;

    public UserLevel() {
    }

    public UserLevel(int masterId, MastersLevel level, boolean isActive) {
        this.masterId = masterId;
        this.level = level;
        this.isActive = isActive;
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

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
