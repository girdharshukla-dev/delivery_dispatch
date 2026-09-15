package com.girdharshukla.deliverymatch.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girdharshukla.deliverymatch.models.User;
import com.girdharshukla.deliverymatch.services.JwtService;
import com.girdharshukla.deliverymatch.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public record UserDto(String email, String password, String role) {
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    public record UserLoginDto(String email, String password) {
    }

    public record UserLoginResponseDto(String token) {
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userLoginDto.email(), userLoginDto.password()));

            String token = jwtService.generateToken(userLoginDto.email());
            return ResponseEntity.ok(new UserLoginResponseDto(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
