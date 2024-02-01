package ua.vspelykh.usermicroservice.controller.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.vspelykh.usermicroservice.controller.request.validation.ValidPassword;
import ua.vspelykh.usermicroservice.controller.request.validation.ValidRepeatPassword;

import java.time.LocalDate;

import static ua.vspelykh.usermicroservice.controller.request.RegexConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidPassword
@ValidRepeatPassword
public class RegistrationRequest {

    @NotBlank
    @Pattern(regexp = USER_NAME_REGEX, message = "Invalid name format.")
    private String firstName;

    @NotBlank
    @Pattern(regexp = USER_NAME_REGEX, message = "Invalid surname format.")
    private String lastName;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX, message = "Invalid email format.")
    private String email;

    @NotBlank
    @Pattern(regexp = NUMBER_REGEX, message = "Invalid phone number.")
    private String number;

    @NotNull
    private LocalDate birthday;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordRepeat;
}
