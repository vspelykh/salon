package ua.vspelykh.usermicroservice.controller.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.vspelykh.usermicroservice.controller.RegistrationRequest;

import static ua.vspelykh.usermicroservice.controller.request.RegexConstants.PASS_NO_SPACES_REGEX;
import static ua.vspelykh.usermicroservice.controller.request.RegexConstants.PASS_REGEX;

public class PasswordValidator implements ConstraintValidator<ValidPassword, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext context) {
        return registrationRequest.getPassword().matches(PASS_REGEX) &&
                registrationRequest.getPassword().matches(PASS_NO_SPACES_REGEX);
    }
}
