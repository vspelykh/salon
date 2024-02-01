package ua.vspelykh.usermicroservice.controller.mapper;

import org.mapstruct.Mapper;
import ua.vspelykh.usermicroservice.controller.request.RegistrationRequest;
import ua.vspelykh.usermicroservice.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRegistrationRequest(RegistrationRequest registrationRequest);
}
