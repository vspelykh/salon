package ua.vspelykh.salon.model;

import java.io.Serializable;

public class UsersLevel implements Serializable {

    private User master;
    private MastersLevel level;

    public UsersLevel() {
    }

    public UsersLevel(User master, MastersLevel level) {
        this.master = master;
        this.level = level;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public MastersLevel getLevel() {
        return level;
    }

    public void setLevel(MastersLevel level) {
        this.level = level;
    }
}
