package com.project.airesumebuilder.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;  // ✅ FIXED

    private Long userId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String thumbnailLink;

    // ================== EMBEDDED ==================

    @Embedded
    private Template template;

    @Embedded
    private ProfileInfo profileInfo;

    @Embedded
    private ContactInfo contactInfo;

    // ================== COLLECTIONS ==================

    @ElementCollection
    private List<WorkExperience> workExperience;

    @ElementCollection
    private List<Education> education;

    @ElementCollection
    private List<Skill> skills;

    @ElementCollection
    private List<Project> projects;

    @ElementCollection
    private List<Certification> certifications;

    @ElementCollection
    private List<Language> languages;

    @ElementCollection
    @CollectionTable(name = "resume_interests", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "interest")
    private List<String> interests;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================== INNER CLASSES ==================

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Template {
        private String theme;

        @ElementCollection
        private List<String> colorPalette;  // ✅ FIXED
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProfileInfo {
        @Column(columnDefinition = "TEXT")
        private String profilePreviewUrl;
        private String fullName;
        private String designation;
        @Column(columnDefinition = "TEXT")
        private String summary;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ContactInfo {
        private String email;
        private String phone;
        private String location;
        private String linkedin;
        private String github;
        private String website;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WorkExperience {
        private String company;
        private String role;
        private String startDate;
        private String endDate;
        @Column(columnDefinition = "TEXT")
        private String description;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Education {
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Skill {
        private String name;
        private Integer progress;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Project {
        private String title;
        @Column(columnDefinition = "TEXT")
        private String description;
        private String github;
        private String liveDemo;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Certification {
        private String title;
        private String issuer;
        private String year;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Language {
        private String name;
        private Integer progress;
    }
}