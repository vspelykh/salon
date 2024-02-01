package ua.vspelykh.usermicroservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.vspelykh.usermicroservice.model.enums.Role;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleChangeRequest {

    private UUID userId;
    private RoleAction action;
    private Role role;
}