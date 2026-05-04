package com.hit.academic_ref.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hit.academic_ref.entity.User;
import com.hit.academic_ref.repository.UserRepository;

@Configuration
public class DataSeederConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            if (userRepository.findByUsername(adminUsername).isPresent()) {
                return;
            }

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@academic-ref.local");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);

            userRepository.save(admin);
        };
    }
}