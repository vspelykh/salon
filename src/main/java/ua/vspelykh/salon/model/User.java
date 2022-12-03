package ua.vspelykh.salon.model;

import java.util.HashSet;
import java.util.Set;

public class User extends AbstractBaseEntity {

    private String name;
    private String surname;
    private String email;
    private String number;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Integer id, String name, String surname, String email, String number, String password) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.number = number;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
