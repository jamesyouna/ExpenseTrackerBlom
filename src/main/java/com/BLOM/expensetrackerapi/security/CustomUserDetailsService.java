package com.BLOM.expensetrackerapi.security;

import com.BLOM.expensetrackerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// take an email -> find that user in the databasee -> convert User enttity into spring security format
// user details is provided by spring security
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override // changed to email (username = email )
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        com.BLOM.expensetrackerapi.entity.User existingUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found for the email: " + email
                                )
                        );

        return org.springframework.security.core.userdetails.User
                .withUsername(existingUser.getEmail())
                .password(existingUser.getPassword()) //takes hashed password
                .roles(existingUser.getRole().name())
                .build();
    } // converting into spring security format like a mapper
}