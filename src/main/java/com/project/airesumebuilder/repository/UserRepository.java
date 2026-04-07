package com.project.airesumebuilder.repository;

// Importing User entity (represents users table)
import com.project.airesumebuilder.entity.User;

// Spring Data JPA repository (provides built-in CRUD methods)
import org.springframework.data.jpa.repository.JpaRepository;

// Optional is used to avoid null pointer issues
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
// JpaRepository<Entity, ID Type>
// Your User ID is Long → so it must be <User, Long> (NOT String)

    Optional<User> findByEmail(String email); // Custom query method, Finds user by email
    // Returns Optional<User> → avoids null (safe handling)

    Boolean existsByEmail(String email); //Checks if email already exists in DB
    // Returns true/false (used for validation like duplicate email check)

    Optional<User> findByVerificationToken(String verificationToken);
}
