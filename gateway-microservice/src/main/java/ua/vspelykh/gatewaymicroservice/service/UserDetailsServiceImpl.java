package ua.vspelykh.gatewaymicroservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.vspelykh.gatewaymicroservice.controller.AuthClient;
import ua.vspelykh.gatewaymicroservice.model.User;
import ua.vspelykh.gatewaymicroservice.model.UserPrincipal;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return loadUserDetailsByUserId(userId);
    }

    public UserPrincipal loadUserDetailsByUserId(String userId) {
        User userProfileDto = authClient.findUserById(UUID.fromString(userId));
        return new UserPrincipal(userProfileDto);
    }
}
