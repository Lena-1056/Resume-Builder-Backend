package com.project.airesumebuilder.entity;

// JPA annotations for database mapping
import jakarta.persistence.*;

// Lombok annotations to reduce boilerplate code
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Hibernate annotations for automatic timestamps
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// Java time classes
import java.time.LocalDateTime;

@Data // Generates getters, setters, toString(), equals(), hashCode() and Saves you from writing boilerplate code
@NoArgsConstructor // Required by JPA (Hibernate needs a default constructor)
@AllArgsConstructor // Creates constructor with all fields
@Builder // Allows creating objects like: User user = User.builder().name("abc").email("x").build();
@Entity // Marks this class as a database entity (table)
@Table(name = "users") // Specifies table name in DB (users table)
public class User {

    @Id // Marks this field as primary key
    // Auto-generates ID using database (PostgreSQL BIGINT auto increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Column cannot be NULL in database
    private String name;

    @Column(unique = true, nullable = false) // Email must be unique and cannot be NULL
    private String email;

    @Column(nullable = false) // Password cannot be NULL
    private String password;

    private boolean emailVerified = false; // Indicates whether email is verified (default = false)

    private String verificationToken; // Token used for email verification process

    private LocalDateTime verificationExpires; // Expiry time for verification token

    @CreationTimestamp // Automatically sets value when record is first created (INSERT)
    @Column(updatable = false) // Prevents this field from being updated later
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates value whenever record is updated (UPDATE)
    private LocalDateTime updatedAt;


}
