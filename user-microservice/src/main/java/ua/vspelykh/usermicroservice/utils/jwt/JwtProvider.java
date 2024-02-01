package ua.vspelykh.usermicroservice.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.vspelykh.usermicroservice.controller.dto.UserInfo;
import ua.vspelykh.usermicroservice.controller.request.RefreshRequest;
import ua.vspelykh.usermicroservice.controller.response.LoginResponse;
import ua.vspelykh.usermicroservice.exception.AuthenticationException;
import ua.vspelykh.usermicroservice.model.entity.User;
import ua.vspelykh.usermicroservice.model.enums.Role;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class JwtProvider {

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final String secret;

    private final JwtParser jwtParser;

    private static final Integer ACCESS_TOKEN_LIFETIME = 10;
    private static final Integer REFRESH_TOKEN_LIFETIME = 24 * 7 * 60;

    public JwtProvider(@Value("${JWT_SECRET}") String secret) {
        this.secret = secret;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))).build();
    }

    public LoginResponse createLoginResponse(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .accessToken(createAccessToken(user))
                .refreshToken(generateRefreshToken(user.getId()))
                .build();
    }

    public LoginResponse refreshAccessToken(String refreshToken, User user) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("Refresh token is expired");
            }
            return LoginResponse.builder()
                    .email(user.getEmail())
                    .accessToken(createAccessToken(user))
                    .refreshToken(refreshToken)
                    .build();
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("Invalid refresh token");
        }
    }

    private String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_LIFETIME));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }

    private String generateRefreshToken(@NonNull UUID id) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(REFRESH_TOKEN_LIFETIME));
        return Jwts.builder().setSubject(id.toString())
                .setExpiration(tokenValidity)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
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

    public Optional<String> resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        return bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX) ?
                Optional.of(bearerToken.substring(TOKEN_PREFIX.length())) : Optional.empty();
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

    public UserInfo getUserInfo(Map<String, String> headers) {
        return UserInfo.builder()
                .userId(UUID.fromString(getUserIdFromToken(headers)))
                .roles(getUserRoleFromToken(headers))
                .build();
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

    public String getUserEmailFromToken(Map<String, String> headers) {
        return getParamFromToken(headers, "email");
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String getUserIdFromRefreshToken(RefreshRequest refreshRequest) {
        return this.getAllClaimsFromToken(refreshRequest.getRefreshToken()).getSubject();
    }
}
