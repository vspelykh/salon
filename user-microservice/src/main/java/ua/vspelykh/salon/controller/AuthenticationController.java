package ua.vspelykh.salon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/login")
public class AuthenticationController {

    @PostMapping
    ResponseEntity<Map<String, String>> authenticate() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
