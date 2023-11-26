package ua.vspelykh.salon.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import ua.vspelykh.salon.model.enums.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static ua.vspelykh.salon.utils.Columns.*;
import static ua.vspelykh.salon.utils.Tables.USERS;
import static ua.vspelykh.salon.utils.Tables.USER_ROLES;

@Entity
@Table(name = USERS)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends AbstractEntity {

    @Column(name = FIRST_NAME)
    private String firstName;

    @Column(name = LAST_NAME)
    private String lastName;

    @Column(name = EMAIL)
    private String email;

    @Column(name = NUMBER)
    private String number;

    @Column(name = BIRTHDAY)
    private LocalDate birthday;

    @Column(name = PASSWORD)
    private String password;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = USER_ROLES, joinColumns = @JoinColumn(name = USER_ID))
    @Column(name = ROLE)
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}