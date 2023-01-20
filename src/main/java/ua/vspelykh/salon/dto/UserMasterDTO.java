package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;

public class UserMasterDTO {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String number;
    private String level;
    private String about;

    public UserMasterDTO() {
    }

    public static UserMasterDTO build(User user, UserLevel userLevel){
        UserMasterDTO dto = new UserMasterDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setNumber(user.getNumber());
        dto.setLevel(String.valueOf(userLevel.getLevel()));
        dto.setAbout(userLevel.getAbout());
        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "UserMasterDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
