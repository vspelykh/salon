package ua.vspelykh.usermicroservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.vspelykh.usermicroservice.controller.dto.UserProfileDto;
import ua.vspelykh.usermicroservice.controller.request.LoginRequest;
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
        String token = jwtProvider.createAccessToken(user);

        LoginResponse loginRes = new LoginResponse(email, token);
        return ResponseEntity.ok(loginRes);
    }

    @PostMapping("/login/id")
    ResponseEntity<UserProfileDto> findUserById(@RequestBody UUID id) {
        UserProfileDto user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/test")
    ResponseEntity<String> test() {
        return ResponseEntity.ok("YEP!");
    }

    @GetMapping("/auth")
    ResponseEntity<String> auth() {
        return ResponseEntity.ok("YEP!2");
    }
}