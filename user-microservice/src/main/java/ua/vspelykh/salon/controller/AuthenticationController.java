package ua.vspelykh.salon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.repository.UserRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/login")
public class AuthenticationController {

    private final UserRepository userRepository;

    @GetMapping
    ResponseEntity<List<User>> authenticate() {
        List<User> all = userRepository.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
