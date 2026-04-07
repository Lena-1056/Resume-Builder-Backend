package com.project.airesumebuilder.dto;

// Lombok annotations to reduce boilerplate code
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Java time class for timestamps
import java.time.LocalDateTime;

@Data // Generates getters, setters, toString(), equals(), hashCode()
@NoArgsConstructor // Default constructor (needed when converting object to JSON)
@AllArgsConstructor // Constructor with all fields
@Builder // Allows building object like: AuthResponse res = AuthResponse.builder().email("x").build();
public class AuthResponse {

    private Long id; // Unique user ID (primary key from database)

    private String name; // User's name

    private String email; // User's email (used for login/identification)

    private boolean emailVerified; // Indicates if user has verified their email

    private String token; // Authentication token (usually JWT) This is sent after login/register for secure API access

    private LocalDateTime createdAt; // Timestamp when user account was created

    private LocalDateTime updatedAt; // Timestamp when user details were last updated
}
