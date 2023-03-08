package ua.vspelykh.salon.model.dto;

import lombok.*;
import ua.vspelykh.salon.model.entity.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents data transfer object class for the user information.
 *
 * @version 1.0
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class UserDto {

    private final int id;
    private final String name;
    private final String surname;
    private final String email;
    private final String number;
    private final LocalDate birthday;
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return name + " " + surname + "(" + number + ")";
    }
}
