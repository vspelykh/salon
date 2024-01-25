package ua.vspelykh.usermicroservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import ua.vspelykh.usermicroservice.model.enums.Role;
import ua.vspelykh.usermicroservice.utils.Columns;
import ua.vspelykh.usermicroservice.utils.Tables;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = Tables.USERS)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends AbstractEntity {

    @Column(name = Columns.FIRST_NAME)
    private String firstName;

    @Column(name = Columns.LAST_NAME)
    private String lastName;

    @Column(name = Columns.EMAIL)
    private String email;

    @Column(name = Columns.NUMBER)
    private String number;

    @Column(name = Columns.BIRTHDAY)
    private LocalDate birthday;

    @Column(name = Columns.PASSWORD)
    private String password;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = Tables.USER_ROLES, joinColumns = @JoinColumn(name = Columns.USER_ID))
    @Column(name = Columns.ROLE)
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