package ua.vspelykh.salon.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ua.vspelykh.salon.model.entity.Role;

import java.util.Set;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserDto {

    private final int id;
    private final String name;
    private final String surname;
    private final String email;
    private final String number;
    private Set<Role> roles;

    @Override
    public String toString() {
        return name + " " + surname + "(" + number + ")";
    }
}
