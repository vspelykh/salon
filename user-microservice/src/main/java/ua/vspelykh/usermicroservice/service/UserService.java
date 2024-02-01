package ua.vspelykh.usermicroservice.service;

import ua.vspelykh.usermicroservice.controller.request.RegistrationRequest;
import ua.vspelykh.usermicroservice.model.entity.User;

import java.util.UUID;

public interface UserService {

    User findUserById(UUID id);


    User findByEmail(String email);

    User registerAsClient(RegistrationRequest registrationRequest);
}
