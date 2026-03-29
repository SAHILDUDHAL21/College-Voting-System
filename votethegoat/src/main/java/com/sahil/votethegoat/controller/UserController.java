package com.sahil.votethegoat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sahil.votethegoat.model.User;
import com.sahil.votethegoat.service.OtpService;
import com.sahil.votethegoat.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            // Send OTP to the user's email
            otpService.generateAndSendOtp(user.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration successful! OTP sent to your email.");
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        boolean verified = otpService.verifyOtp(email, otp);
        Map<String, String> response = new HashMap<>();

        if (verified) {
            userService.markUserVerified(email);
            response.put("message", "Email verified successfully! You can now login.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired OTP!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            otpService.generateAndSendOtp(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP resent to your email.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to send OTP.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        try {
            String role = creds.get("role");
            String emailOrId = creds.get("emailOrId");
            String password = creds.get("password");

            User user = userService.loginUser(emailOrId, password, role);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/voters")
    public ResponseEntity<?> getVoters() {
        return ResponseEntity.ok(userService.getVoters());
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestBody User adminUser) {
        try {
            User newAdmin = userService.addAdmin(adminUser);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin " + newAdmin.getEmail() + " successfully added!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/password-otp")
    public ResponseEntity<?> requestPasswordOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            otpService.generateAndSendOtp(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP for password change sent to your email.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to send OTP.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        try {
            // Check if password update is requested
            String newPassword = (String) payload.get("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                String email = (String) payload.get("email");
                String otp = (String) payload.get("otp");
                
                if (otp == null || otp.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "OTP is required to change password!"));
                }
                
                if (!otpService.verifyOtp(email, otp)) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP!"));
                }
            }

            // Convert payload back to User object for the service
            User userDetails = new User();
            userDetails.setFullname((String) payload.get("fullname"));
            userDetails.setCollegeId((String) payload.get("collegeId"));
            userDetails.setClassName((String) payload.get("className"));
            userDetails.setDepartment((String) payload.get("department"));
            userDetails.setPassword(newPassword);

            User updatedUser = userService.updateProfile(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
