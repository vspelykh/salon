package ua.vspelykh.usermicroservice.service;

import ua.vspelykh.usermicroservice.controller.dto.UserProfileDto;
import ua.vspelykh.usermicroservice.model.entity.User;

import java.util.UUID;

public interface UserService {

    UserProfileDto findUserById(UUID id);


    User findByEmail(String email);
}
