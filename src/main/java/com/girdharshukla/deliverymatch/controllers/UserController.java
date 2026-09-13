package com.girdharshukla.deliverymatch.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public record UserDto(String email, String password){}

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }


    @PostMapping("/login")
    public String loginUser(@RequestBody UserDto userDto){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password()));

        return (auth.isAuthenticated() ? "SUCCESS " : "FAIL ") + jwtService.generateToken(userDto.email());
    }
}
