package com.girdharshukla.deliverymatch.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.girdharshukla.deliverymatch.models.User;
import com.girdharshukla.deliverymatch.models.UserPrincipal;
import com.girdharshukla.deliverymatch.repositories.UserRepository;

@Component
public class MyUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null) throw new UsernameNotFoundException("User with email not found");

        return new UserPrincipal(user);
    }
    
}
