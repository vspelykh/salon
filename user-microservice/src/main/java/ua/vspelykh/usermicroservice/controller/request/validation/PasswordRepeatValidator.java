package ua.vspelykh.usermicroservice.controller.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.vspelykh.usermicroservice.controller.request.RegistrationRequest;

public class PasswordRepeatValidator implements ConstraintValidator<ValidRepeatPassword, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext context) {
        return registrationRequest.getPassword().equals(registrationRequest.getPasswordRepeat());
    }
}
