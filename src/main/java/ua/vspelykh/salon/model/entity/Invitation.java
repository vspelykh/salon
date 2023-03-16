package ua.vspelykh.salon.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * This class represents an invitation sent to a user to join the system as an administrator or master.
 * It extends the AbstractBaseEntity class and inherits its "id" field.
 * The class uses Lombok annotations to generate getters, setters, no-args constructor,
 * an implementation of the builder pattern, and an implementation of the equals and hashCode methods
 * that only take into account the explicitly included fields.
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class Invitation extends AbstractBaseEntity {

    private String email;
    private Role role;
    private String key;
    private LocalDate date;

    public Invitation(String email, Role role, String key) {
        this.email = email;
        this.role = role;
        this.key = key;
    }
}
