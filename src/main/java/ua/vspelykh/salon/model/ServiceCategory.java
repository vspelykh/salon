package ua.vspelykh.salon.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ServiceCategory extends AbstractBaseEntity {

    private String name;
    private String nameUa;

    public ServiceCategory(String name, String nameUa) {
        this.name = name;
        this.nameUa = nameUa;
    }

    public ServiceCategory(Integer id, String name, String nameUa) {
        super(id);
        this.name = name;
        this.nameUa = nameUa;
    }
}
