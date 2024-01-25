package ua.vspelykh.gatewaymicroservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ua.vspelykh.gatewaymicroservice.config.FeignConfig;
import ua.vspelykh.gatewaymicroservice.model.User;

import java.util.UUID;

@FeignClient(name = "user-microservice", url = "${APP.SERVER.URL}:8200/api/v1/login", configuration = FeignConfig.class)
public interface AuthClient {

    @PostMapping(path = "/id", consumes = "application/json")
    User findUserById(UUID id);
}
