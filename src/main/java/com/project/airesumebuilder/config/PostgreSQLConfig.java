package com.project.airesumebuilder.config;

// Marks this class as a Spring configuration class
import org.springframework.context.annotation.Configuration;

// Enables JPA auditing features (like auto timestamps using @CreatedDate, @LastModifiedDate)
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // Tells Spring Boot that this class contains configuration settings
// Spring will scan and apply this automatically at startup
@EnableJpaAuditing
// Enables JPA Auditing in your application
// Required if you use:
//    @CreatedDate  → automatically sets creation time
//    @LastModifiedDate → automatically updates modification time
// Without this, those annotations will NOT work
public class PostgreSQLConfig {
}
