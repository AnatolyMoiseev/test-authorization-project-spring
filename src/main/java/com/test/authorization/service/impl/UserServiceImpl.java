package com.test.authorization.service.impl;

import com.test.authorization.dto.SignUpDto;
import com.test.authorization.exception.AppException;
import com.test.authorization.exception.ResourceNotFoundException;
import com.test.authorization.model.Role;
import com.test.authorization.model.User;
import com.test.authorization.model.enums.Status;
import com.test.authorization.repository.RoleRepository;
import com.test.authorization.repository.UserRepository;
import com.test.authorization.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(SignUpDto signUpDto) {
        User user = new User();

        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AppException("User Role not set."));
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(LocalDate.now());
        user.setUpdated(LocalDate.now());

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("user", "username", username));
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted");
    }
}
