package com.sahil.votethegoat.config;

import com.sahil.votethegoat.model.User;
import com.sahil.votethegoat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // Create hardcoded admin if not exists
        Optional<User> adminOpt = userRepository.findByEmail("admin@college.edu");
        if (adminOpt.isEmpty()) {
            User admin = new User();
            admin.setFullname("Super Admin");
            admin.setCollegeId("ADM001");
            admin.setEmail("admin@college.edu");
            admin.setPassword(passwordEncoder.encode("admin123")); // Default password
            admin.setRole("admin");
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("Hardcoded Admin initialized.");
        }
    }
}
