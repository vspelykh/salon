package ua.vspelykh.salon.model;

import java.io.Serializable;

public class UserLevel implements Serializable {

    private int masterId;
    private MastersLevel level;
    private String about;
    private String aboutUa;
    private boolean isActive;

    public UserLevel() {
    }

    public UserLevel(int masterId, MastersLevel level, String about, String aboutUa, boolean isActive) {
        this.masterId = masterId;
        this.level = level;
        this.about = about;
        this.aboutUa = aboutUa;
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

    public String getAboutUa() {
        return aboutUa;
    }

    public void setAboutUa(String aboutUa) {
        this.aboutUa = aboutUa;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "UserLevel{" +
                "masterId=" + masterId +
                ", level=" + level +
                '}';
    }
}
