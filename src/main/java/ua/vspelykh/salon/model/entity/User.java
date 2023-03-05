package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * The User class represents a user entity in the application.
 * It extends the AbstractBaseEntity class and includes additional fields for user details.
 * <p>
 * Use the builder pattern to create new instances of this class.
 *
 * @version 1.0
 */
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
}
