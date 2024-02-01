package ua.vspelykh.gatewaymicroservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ua.vspelykh.gatewaymicroservice.config.AuthenticationManager;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        Mono<String> stringMono = Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        return stringMono.flatMap(this::getSecurityContext);
    }

    private Mono<? extends SecurityContext> getSecurityContext(String token) {
        String authToken = token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
        Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
        return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
    }
}