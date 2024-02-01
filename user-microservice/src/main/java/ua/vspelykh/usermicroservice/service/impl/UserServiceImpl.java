package ua.vspelykh.usermicroservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vspelykh.usermicroservice.controller.RegistrationRequest;
import ua.vspelykh.usermicroservice.controller.mapper.UserMapper;
import ua.vspelykh.usermicroservice.exception.ResourceNotFoundException;
import ua.vspelykh.usermicroservice.model.entity.User;
import ua.vspelykh.usermicroservice.model.enums.Role;
import ua.vspelykh.usermicroservice.repository.UserRepository;
import ua.vspelykh.usermicroservice.service.UserService;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found by id: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found by email: " + email));
    }

    @Override
    @Transactional
    public User registerAsClient(RegistrationRequest registrationRequest) {
        User user = userMapper.fromRegistrationRequest(registrationRequest);
        user.setRoles(Set.of(Role.CLIENT));
        return userRepository.save(user);

    }
}
