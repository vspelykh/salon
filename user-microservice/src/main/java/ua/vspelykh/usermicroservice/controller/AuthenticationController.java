package ua.vspelykh.usermicroservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.vspelykh.usermicroservice.controller.request.LoginRequest;
import ua.vspelykh.usermicroservice.controller.request.RefreshRequest;
import ua.vspelykh.usermicroservice.controller.request.RegistrationRequest;
import ua.vspelykh.usermicroservice.controller.response.LoginResponse;
import ua.vspelykh.usermicroservice.model.entity.User;
import ua.vspelykh.usermicroservice.service.UserService;
import ua.vspelykh.usermicroservice.utils.jwt.JwtProvider;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ua.vspelykh.usermicroservice.controller.response.SwaggerConstants.Code.CODE_200;
import static ua.vspelykh.usermicroservice.controller.response.SwaggerConstants.Code.CODE_404;
import static ua.vspelykh.usermicroservice.controller.response.SwaggerConstants.Message.MESSAGE_200;
import static ua.vspelykh.usermicroservice.controller.response.SwaggerConstants.Message.MESSAGE_404;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1", produces = APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;


    @PostMapping("/login")
    @Operation(summary = "Login in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = CODE_200, description = MESSAGE_200, content =
            {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = CODE_404, description = MESSAGE_404)
    })
    @Parameter(name = "Source", in = ParameterIn.HEADER, description = "disable cors", required = true,
            schema = @Schema(type = "string", allowableValues = {"swg"}))
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                        (loginRequest.getEmail(), loginRequest.getPassword()));
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(jwtProvider.createLoginResponse(user));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Get new access token by refresh token")
    @Parameter(name = "Source", in = ParameterIn.HEADER, description = "disable cors", required = true,
            schema = @Schema(type = "string", allowableValues = {"swg"}))
    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestBody RefreshRequest refreshRequest) {
        String userId = jwtProvider.getUserIdFromRefreshToken(refreshRequest);
        User user = userService.findUserById(UUID.fromString(userId));
        LoginResponse loginResponse = jwtProvider.refreshAccessToken(refreshRequest.getRefreshToken(), user);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/token")
    @Parameter(name = "Source", in = ParameterIn.HEADER, description = "disable cors", required = true,
            schema = @Schema(type = "string", allowableValues = {"swg"}))
    public ResponseEntity<Boolean> isTokenValid(@RequestHeader Map<String, String> headers) {
        String accessToken = jwtProvider.resolveToken(headers);
        return ResponseEntity.ok(jwtProvider.validateToken(accessToken));
    }

    @PostMapping("/registration")
    @Operation(summary = "Registration in the system as a client")
    @Parameter(name = "Source", in = ParameterIn.HEADER, description = "disable cors", required = true,
            schema = @Schema(type = "string", allowableValues = {"swg"}))
    public ResponseEntity<LoginResponse> registerAsClient(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User registeredUser = userService.registerAsClient(registrationRequest);
        return ResponseEntity.ok(jwtProvider.createLoginResponse(registeredUser));
    }
}