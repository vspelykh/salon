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
}
