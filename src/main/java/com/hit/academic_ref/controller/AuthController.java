package com.hit.academic_ref.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.hit.academic_ref.security.JwtUtil;
import com.hit.academic_ref.entity.User;
import com.hit.academic_ref.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        // Return token + role so frontend knows what to show
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", user.getRole(),
            "username", user.getUsername()
        ));
    }

    // Admin creates users — students don't self-register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // convert role string to enum
        user.setRole(User.Role.valueOf(request.getRole()));
        userRepository.save(user);
        return ResponseEntity.ok("User created");
    }

}
