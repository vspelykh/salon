package ua.vspelykh.usermicroservice.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.vspelykh.usermicroservice.controller.dto.UserProfileDto;
import ua.vspelykh.usermicroservice.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserProfileDto toProfileDto(User user);

}
