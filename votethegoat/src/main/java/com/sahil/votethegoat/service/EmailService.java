package com.sahil.votethegoat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        System.out.println("DEBUG: OTP for " + to + " is " + otp);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Voting Portal - Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp +
                "\n\nThis OTP is valid for 5 minutes." +
                "\n\nIf you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}
