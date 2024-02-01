package ua.vspelykh.gatewaymicroservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.vspelykh.gatewaymicroservice.exception.AuthenticationException;
import ua.vspelykh.gatewaymicroservice.model.Role;

import java.util.*;
import java.util.function.Function;


@Component
public class JwtProvider {

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final String secret;

    private final JwtParser jwtParser;

    public JwtProvider(@Value("${JWT_SECRET}") String secret) {
        this.secret = secret;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))).build();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            Optional<String> token = resolveToken(req);
            return token.map(this::parseJwtClaims).orElseThrow(() -> new AuthenticationException("Authentication error"));
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationException("Token expired");
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token");
        }
    }

    public boolean validateToken(String token) {
        Claims claims;
        try {
            claims = this.getAllClaimsFromToken(token);
        } catch (Exception var4) {
            return false;
        }

        return claims.getExpiration().after(new Date());
    }

    public Optional<String> resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        return bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX) ?
                Optional.of(bearerToken.substring(TOKEN_PREFIX.length())) : Optional.empty();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(Map<String, String> headers) {

        String bearer = Optional.ofNullable(headers.get(TOKEN_HEADER)).orElse(headers.get("authorization"));
        return bearer != null && bearer.startsWith(TOKEN_PREFIX) ? bearer.substring(TOKEN_PREFIX.length()) : null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims != null && claims.getExpiration() != null && claims.getExpiration().after(new Date());
    }

    public String getUserIdFromToken(Map<String, String> headers) {
        return getParamFromToken(headers, "id");
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUserIdFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    private String getParamFromToken(Map<String, String> headers, String param) {
        try {
            String token = this.resolveToken(headers);
            return this.getAllClaimsFromToken(token).get(param).toString();
        } catch (JwtException | IllegalArgumentException var3) {
            throw new AuthenticationException("Token is incorrect or absent");
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .build().parseClaimsJws(token).getBody();
    }


    public Set<Role> getUserRoleFromToken(Map<String, String> headers) {
        try {
            String token = this.resolveToken(headers);
            return (Set<Role>) getAllClaimsFromToken(token).get("roles");
        } catch (JwtException | IllegalArgumentException var3) {
            throw new AuthenticationException("Token is incorrect or absent");
        }
    }

    public Set<Role> getUserRoleFromToken(String token) {
        try {
            List<String> rolesList = (List<String>) getAllClaimsFromToken(token).get("roles");
            Set<Role> objects = new HashSet<>();
            for (String role : rolesList) {
                objects.add(Role.valueOf(role));
            }
            return objects;

        } catch (JwtException | IllegalArgumentException var3) {
            throw new AuthenticationException("Token is incorrect or absent");
        }
    }

    public String getUserEmailFromToken(Map<String, String> headers) {
        return getParamFromToken(headers, "email");
    }

    public String getUserEmailFromToken(String token) {
        return this.getAllClaimsFromToken(token).get("email").toString();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
