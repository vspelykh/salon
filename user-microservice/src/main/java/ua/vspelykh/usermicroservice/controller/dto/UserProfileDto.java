package ua.vspelykh.usermicroservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.vspelykh.usermicroservice.model.enums.Role;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserProfileDto {

    private UUID id;
    private String email;
    private Set<Role> roles;
    private String password;
}