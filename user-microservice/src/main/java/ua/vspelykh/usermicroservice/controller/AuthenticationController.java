package ua.vspelykh.usermicroservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vspelykh.usermicroservice.controller.request.LoginRequest;
import ua.vspelykh.usermicroservice.controller.request.RefreshRequest;
import ua.vspelykh.usermicroservice.controller.response.LoginResponse;
import ua.vspelykh.usermicroservice.model.entity.User;
import ua.vspelykh.usermicroservice.service.UserService;
import ua.vspelykh.usermicroservice.utils.jwt.JwtProvider;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1", produces = "application/json")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                        (loginRequest.getEmail(), loginRequest.getPassword()));
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(jwtProvider.createLoginResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestBody RefreshRequest refreshRequest) {
        String userId = jwtProvider.getUserIdFromRefreshToken(refreshRequest);
        User user = userService.findUserById(UUID.fromString(userId));
        LoginResponse loginResponse = jwtProvider.refreshAccessToken(refreshRequest.getRefreshToken(), user);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/registration")
    public ResponseEntity<LoginResponse> registerAsClient(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User registeredUser = userService.registerAsClient(registrationRequest);
        return ResponseEntity.ok(jwtProvider.createLoginResponse(registeredUser));
    }
}