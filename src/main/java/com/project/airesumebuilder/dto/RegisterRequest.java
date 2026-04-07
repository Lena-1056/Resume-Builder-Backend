package com.project.airesumebuilder.dto;

// Validation annotations (used to validate incoming request data)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lombok annotations to reduce boilerplate code
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    // Validates that the input is in proper email format
    @Email(message = "Invalid email format and Email should be valid")
    @NotBlank(message = "Email is required") // Ensures email is not null, empty, or just spaces
    private String email;

    @NotBlank(message = "Name is required") //  Name must not be empty
    @Size(min = 2, max = 15, message = "Name must be between 2 & 15 characters")
    // Name length must be between 2 and 15 characters
    private String name;

    @NotBlank(message = "Password is required") // Password must not be empty
    @Size(min = 6, max = 15, message = "Password must be at least 6 characters and not more than 15 characters")
    // Password length restriction (basic validation)
    private String password;
}
