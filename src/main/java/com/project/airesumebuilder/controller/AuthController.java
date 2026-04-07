// Package for controller layer (handles HTTP requests)
package com.project.airesumebuilder.controller;

import com.project.airesumebuilder.dto.AuthResponse; // Response DTO sent back to client
import com.project.airesumebuilder.dto.LoginRequest;
import com.project.airesumebuilder.dto.RegisterRequest; // Request DTO used to receive input data
import com.project.airesumebuilder.service.AuthService; // Service layer containing business logic
import jakarta.validation.Valid; // Enables validation (@NotBlank, @Email, etc.)
import lombok.RequiredArgsConstructor; // Generates constructor for final fields (dependency injection)
import lombok.extern.slf4j.Slf4j; // Enables logging (log.info, log.error)
import org.springframework.http.HttpStatus; // HTTP status codes (201, 400, etc.)
import org.springframework.http.ResponseEntity; // Used to return response with status + body
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Objects;
import static com.project.airesumebuilder.util.AppConstants.*;

@RestController // Indicates this class handles REST API requests
@RequiredArgsConstructor // Injects AuthService automatically via constructor
@Slf4j // Enables logging support
@RequestMapping(AUTH_CONTROLLER) // Base URL for all endpoints in this controller
public class AuthController {

    private final AuthService authService; // Service used to handle business logic
    // private final FileUploadService fileUploadService;

    @PostMapping(REGISTER) // Handles POST request at /api/auth/register
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Inside AuthController: register() {} ", request);
        // @RequestBody → converts JSON to Java object
        // @Valid → triggers validation on RegisterRequest
        AuthResponse response = authService.register(request); // Calls service layer to register user
        log.info("Response from register: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);// Returns HTTP 201 (Created) with response data
    }

    // Controller method for verifying email using endpoint
    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        log.info("Inside AuthController: verifyEmail() {} ", token);
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Email verified successfully"));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("Inside AuthController: login() {} ", request);
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> body) {
        log.info("Inside AuthController: resendVerificationCode() {} ", body);
        // Step 1: Get the email from the request
        String email = body.get("email");

        // Step 2: Add the validations
        if(Objects.isNull(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        // Step 3: Call the service method to resend the verification link
        authService.resendVerification(email);

        // Step 4: Return the response
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("success", "Verification email sent"));
    }

    @GetMapping(PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication) {
        log.info("Inside AuthController: getProfile() {} ", authentication.getName());
        // Step 1: Get the principal object
        Object principalObject = authentication.getPrincipal();

        // Step 2: Call the service method
        AuthResponse currentProfile = authService.getProfile(principalObject);

        // Step 3: Return the response
        return ResponseEntity.ok(currentProfile);
    }


}