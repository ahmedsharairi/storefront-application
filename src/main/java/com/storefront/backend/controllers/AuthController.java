package com.storefront.backend.controllers;

import com.storefront.backend.models.User;
import com.storefront.backend.security.JwtUtil;
import com.storefront.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.findUser(user);
    }

    @PostMapping("/verify")
    public Boolean verifyToken(@RequestBody String token) {
        return jwtUtil.validateJwtToken(token);
    }
}
