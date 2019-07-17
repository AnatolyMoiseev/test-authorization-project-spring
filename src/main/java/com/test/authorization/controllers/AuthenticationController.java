package com.test.authorization.controllers;

import com.test.authorization.dto.SignInDto;
import com.test.authorization.dto.SignUpDto;
import com.test.authorization.exception.BadRequestException;
import com.test.authorization.model.User;
import com.test.authorization.repository.UserRepository;
import com.test.authorization.security.jwt.JwtTokenProvider;
import com.test.authorization.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody SignInDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String accessToken = jwtTokenProvider.createAccessToken(username, user.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(username);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername()))
            throw new BadRequestException("Username is already taken");

        if (userRepository.existsByEmail(signUpDto.getEmail()))
            throw new BadRequestException("Email Address already in use");

        return ResponseEntity.ok(userService.register(signUpDto));
    }

    @PostMapping("/token")
    public ResponseEntity refreshToken(@RequestParam String refreshToken) {

        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userRepository.findByUsername(username);

        if (user.getRefreshToken().equals(refreshToken)) {
            String newAccessToken = jwtTokenProvider.createAccessToken(username, user.getRoles());
            String newRefreshToken = jwtTokenProvider.createRefreshToken(username);

            Map<Object, Object> response = new HashMap<>();
            response.put("newAccessToken", newAccessToken);
            response.put("newRefreshToken", newRefreshToken);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok("Dura");
    }

}
