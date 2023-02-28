package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class User extends AbstractBaseEntity {

    private String name;
    private String surname;
    private String email;
    private String number;
    private transient String password;
    private Set<Role> roles = new HashSet<>();

    public User(String name, String surname, String email, String number, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.number = number;
        this.password = password;
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
