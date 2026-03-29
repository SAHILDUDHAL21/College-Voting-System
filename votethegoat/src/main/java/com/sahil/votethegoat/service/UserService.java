package com.sahil.votethegoat.service;

import com.sahil.votethegoat.model.User;
import com.sahil.votethegoat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email is already registered!");
        }
        if ("admin".equalsIgnoreCase(user.getRole())) {
            throw new Exception("Direct admin registration is not allowed!");
        }
        if (userRepository.findByCollegeId(user.getCollegeId()).isPresent()) {
            throw new Exception("College ID is already registered!");
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        return userRepository.save(user);
    }

    public User loginUser(String emailOrCollegeId, String password, String role) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(emailOrCollegeId);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByCollegeId(emailOrCollegeId);
        }

        if (userOpt.isEmpty()) {
            throw new Exception("Invalid credentials!");
        }

        User user = userOpt.get();

        // Compare hashed password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid credentials!");
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            throw new Exception("Role mismatch!");
        }

        if (!user.isVerified()) {
            throw new Exception("Email not verified! Please verify your email first.");
        }

        return user;
    }

    public User addAdmin(User adminUser) throws Exception {
        if (userRepository.findByEmail(adminUser.getEmail()).isPresent()) {
            throw new Exception("Email is already registered!");
        }
        if (userRepository.findByCollegeId(adminUser.getCollegeId()).isPresent()) {
            throw new Exception("College ID is already registered!");
        }
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUser.setRole("admin");
        adminUser.setVerified(true);
        return userRepository.save(adminUser);
    }

    public void markUserVerified(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setVerified(true);
            userRepository.save(user);
        }
    }

    public List<User> getVoters() {
        List<User> users = userRepository.findByRole("student");
        users.addAll(userRepository.findByRole("teacher"));
        return users;
    }

    public User updateProfile(String id, User updatedInfo) throws Exception {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found!");
        }
        User user = userOpt.get();
        if (updatedInfo.getFullname() != null && !updatedInfo.getFullname().isEmpty()) {
            user.setFullname(updatedInfo.getFullname());
        }
        if (updatedInfo.getCollegeId() != null && !updatedInfo.getCollegeId().isEmpty()) {
            // Check if another user has this collegeId
            Optional<User> existingUser = userRepository.findByCollegeId(updatedInfo.getCollegeId());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new Exception("College ID is already in use by another account!");
            }
            user.setCollegeId(updatedInfo.getCollegeId());
        }
        if (updatedInfo.getClassName() != null && !updatedInfo.getClassName().isEmpty()) {
            user.setClassName(updatedInfo.getClassName());
        }
        if (updatedInfo.getDepartment() != null && !updatedInfo.getDepartment().isEmpty()) {
            user.setDepartment(updatedInfo.getDepartment());
        }
        // Password update
        if (updatedInfo.getPassword() != null && !updatedInfo.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedInfo.getPassword()));
        }
        return userRepository.save(user);
    }
}
