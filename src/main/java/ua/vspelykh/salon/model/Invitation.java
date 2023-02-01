package ua.vspelykh.salon.model;

import java.time.LocalDate;

public class Invitation extends AbstractBaseEntity {

    private String email;
    private Role role;
    private String key;
    private LocalDate date;

    public Invitation() {
    }

    public Invitation(String email, Role role, String key) {
        this.email = email;
        this.role = role;
        this.key = key;
    }

    public Invitation(Integer id, String email, Role role, String key, LocalDate date) {
        super(id);
        this.email = email;
        this.role = role;
        this.key = key;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
