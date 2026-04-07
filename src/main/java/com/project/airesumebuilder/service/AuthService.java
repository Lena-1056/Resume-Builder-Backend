package com.project.airesumebuilder.service;

import com.project.airesumebuilder.dto.AuthResponse;
import com.project.airesumebuilder.dto.LoginRequest;
import com.project.airesumebuilder.dto.RegisterRequest;
import com.project.airesumebuilder.entity.User;
import com.project.airesumebuilder.exception.ResourceExistsException;
import com.project.airesumebuilder.repository.UserRepository;
import com.project.airesumebuilder.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service // Marks this class as a service layer component
@RequiredArgsConstructor // Automatically creates constructor for final fields (dependency injection)
@Slf4j // Enables logging (log.info, log.error)
public class AuthService {

    private final UserRepository userRepository; //  Injecting repository to interact with database
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.base.url:http://localhost:9090}")
    private String appBaseUrl;

    // Method to register user
    public AuthResponse register(RegisterRequest request) {
        log.info("Inside AuthService: register() {} ", request); //  Logs incoming request for debugging
        if(userRepository.existsByEmail(request.getEmail())) { //  Prevent duplicate users
            throw new ResourceExistsException("User already exists with this email");
        }
        User newUser = toRequest(request); // Convert DTO → Entity
        userRepository.save(newUser); // Save user in database

        // TODO: Send verification email
        sendVerificationEmail(newUser);

        return toResponse(newUser); // Convert Entity → Response DTO
    }

    // Method to send verification email to the registered email upon register
    private void sendVerificationEmail(User newUser) {
        log.info("Inside AuthService: sendVerificationEmail() {} ", newUser);
        try {
            String link = appBaseUrl+"/api/auth/verify-email?token="+newUser.getVerificationToken();
            String html = "<div style='font-family:Arial, sans-serif; padding:20px;'>" +
                    "<h2 style='color:#333;'>Verify your email</h2>" +
                    "<p>Hi " + newUser.getName() + ",</p>" +
                    "<p>Thanks for registering! Please confirm your email to activate your account.</p>" +
                    "<p style='margin:20px 0;'>" +
                    "<a href='" + link + "' " +
                    "style='background-color:#4CAF50; color:white; padding:12px 20px; text-decoration:none; border-radius:5px;'>"
                    + "Verify Email</a></p>" +
                    "<p>If the button doesn't work, copy and paste the link below:</p>" +
                    "<p><a href='" + link + "'>" + link + "</a></p>" +
                    "<hr style='margin:20px 0;'/>" +
                    "<p style='font-size:12px; color:gray;'>This link will expire in 24 hours.</p>" +
                    "<p style='font-size:12px; color:gray;'>If you did not create this account, please ignore this email.</p>" +
                    "<p style='margin-top:20px;'>Thanks,<br/>AI Resume Builder Team</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your email", html);

        } catch (Exception e) {
            log.error("Error while sending verification email", e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    // Converts User entity to API response
    private AuthResponse toResponse(User newUser) {
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .emailVerified(newUser.isEmailVerified())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }

    // Converts RegisterRequest DTO to User entity
    private User toRequest(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Should be encoded in real apps
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString()) // Generates unique token for email verification
                .verificationExpires(LocalDateTime.now().plusHours(24)) // Token expires in 24 hours
                .build();
    }

    // method to verify token
    public void verifyEmail(String token) {
        log.info("Inside AuthService: verifyEmail() {} ", token);
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if(user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired, please request new token");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
    }

    // Method for login
    public AuthResponse login(LoginRequest request) {
        log.info("Inside AuthService: login() {} ", request);
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if(!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid email or password");
        }

        if (!existingUser.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        String token = jwtUtil.generateToken(String.valueOf(existingUser.getId()));

        AuthResponse response = toResponse(existingUser);
        response.setToken(token);
        return response;
    }

    public void resendVerification(String email) {
        // Step 1: Fetch the user account by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Check the email is verified
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        // Step 3: Set the new verification token and expiration time
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));

        // Step 4: Update the user
        userRepository.save(user);

        // Step 5: Resend the verification email
        sendVerificationEmail(user);

    }

    public AuthResponse getProfile(Object principalObject) {
        User existingUser = (User)principalObject;
        return toResponse(existingUser);
    }
}