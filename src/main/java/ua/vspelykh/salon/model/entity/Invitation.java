package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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

    public Invitation(Integer id, String email, Role role, String key, LocalDate date) {
        super(id);
        this.email = email;
        this.role = role;
        this.key = key;
        this.date = date;
    }
}
