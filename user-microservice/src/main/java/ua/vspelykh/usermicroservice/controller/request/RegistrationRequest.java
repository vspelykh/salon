package ua.vspelykh.usermicroservice.controller.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 150)
    @Pattern(regexp = EMAIL_REGEX, message = "Invalid email format.")
    private String email;

    @NotBlank
    @Pattern(regexp = NUMBER_REGEX, message = "Invalid phone number.")
    private String number;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordRepeat;
}
