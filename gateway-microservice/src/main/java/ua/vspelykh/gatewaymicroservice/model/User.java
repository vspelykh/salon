package ua.vspelykh.gatewaymicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private UUID id;
    private String email;
    private String password;
    private Set<Role> roles;
}