package com.storefront.backend.services;

import com.storefront.backend.models.User;
import com.storefront.backend.repositories.UserRepository;
import com.storefront.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void createUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch(Exception e) {
            throw new RuntimeException("Unable to create user:", e);
        }
    }

    public String findUser(User loginRequest) {
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(loginRequest.getUsername()))
                .findFirst();

        if (optionalUser.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            return jwtUtil.generateToken(optionalUser.get().getUsername());
        } else {
            throw new RuntimeException("Invalid login!");
        }
    }

}
