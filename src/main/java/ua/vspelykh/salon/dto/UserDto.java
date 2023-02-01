package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.Role;

import java.util.Set;

public class UserDto {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String number;
    private Set<Role> roles;

    public UserDto(int id, String name, String surname, String email, String number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.number = number;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", name, surname, number);
    }
}
