package ua.vspelykh.usermicroservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vspelykh.usermicroservice.controller.dto.UserProfileDto;
import ua.vspelykh.usermicroservice.controller.mapper.UserMapper;
import ua.vspelykh.usermicroservice.exception.ResourceNotFoundException;
import ua.vspelykh.usermicroservice.model.entity.User;
import ua.vspelykh.usermicroservice.repository.UserRepository;
import ua.vspelykh.usermicroservice.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileDto findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found by id: " + id));
        return userMapper.toProfileDto(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found by email: " + email));
    }
}
