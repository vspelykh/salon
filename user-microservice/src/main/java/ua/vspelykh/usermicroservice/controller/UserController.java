package ua.vspelykh.usermicroservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vspelykh.usermicroservice.controller.request.UserRoleChangeRequest;
import ua.vspelykh.usermicroservice.service.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ua.vspelykh.usermicroservice.utils.SystemConstants.API_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API_V1, produces = APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @PostMapping("/roles")
    @Operation(summary = "Login in the system")
    @Parameter(name = "Source", in = ParameterIn.HEADER, description = "disable cors", required = true,
            schema = @Schema(type = "string", allowableValues = {"swg"}))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> changeRoleOfUser(@RequestBody UserRoleChangeRequest request) {
        userService.updateRoleByUserId(request);
        return ResponseEntity.noContent().build();
    }

}
