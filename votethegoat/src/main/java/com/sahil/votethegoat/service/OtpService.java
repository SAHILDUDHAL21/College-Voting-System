package com.sahil.votethegoat.service;

import com.sahil.votethegoat.model.Otp;
import com.sahil.votethegoat.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void generateAndSendOtp(String email) {
        // Delete any existing OTP for this email
        otpRepository.deleteByEmail(email);

        // Generate a 6-digit OTP
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(passwordEncoder.encode(otpCode)); // Securely hash the OTP
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        // Send the plain text OTP via email
        emailService.sendOtpEmail(email, otpCode);
        
        // Debug print to console for testing locally
        System.out.println("DEBUG: OTP generated for " + email + " is: " + otpCode);
    }

    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> otpOpt = otpRepository.findByEmail(email);
        if (otpOpt.isEmpty()) {
            return false;
        }

        Otp otp = otpOpt.get();
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            return false; // OTP expired
        }

        if (!passwordEncoder.matches(otpCode, otp.getOtp())) {
            return false; // Wrong OTP
        }

        // OTP verified, clean up
        otpRepository.deleteByEmail(email);
        return true;
    }
}
