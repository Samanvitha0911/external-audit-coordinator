package com.internship.tool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.tool.dto.LoginRequest;
import com.internship.tool.security.JwtUtil;
import com.internship.tool.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        return userService.findByUsername(request.getUsername())
                .map(user -> {
                    String storedPassword = user.getPassword();
                    boolean matches = passwordEncoder.matches(request.getPassword(), storedPassword)
                            || request.getPassword().equals(storedPassword);

                    if (matches) {
                        String token = jwtUtil.generateToken(
                                user.getUsername() + ":" + user.getRole()
                        );

                        return ResponseEntity.ok(token);
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(401).body("User not found"));
    }
}

