package com.girdharshukla.deliverymatch.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.controllers.UserController.UserDto;
import com.girdharshukla.deliverymatch.models.User;
import com.girdharshukla.deliverymatch.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User registerUser(UserDto userDto){
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
    
}

