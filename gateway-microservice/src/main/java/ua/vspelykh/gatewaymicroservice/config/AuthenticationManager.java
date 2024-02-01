package ua.vspelykh.gatewaymicroservice.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.vspelykh.gatewaymicroservice.model.Role;
import ua.vspelykh.gatewaymicroservice.util.JwtProvider;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        return Mono.just(jwtProvider.validateToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    Set<Role> roles = jwtProvider.getUserRoleFromToken(authToken);
                    return new UsernamePasswordAuthenticationToken(
                            jwtProvider.getUserEmailFromToken(authToken),
                            null,
                            roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList())
                    );
                });
    }
}