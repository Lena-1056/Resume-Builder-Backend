package com.project.airesumebuilder.controller;

import com.project.airesumebuilder.dto.CreateResumeRequest;
import com.project.airesumebuilder.entity.Resume;
import com.project.airesumebuilder.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static com.project.airesumebuilder.util.AppConstants.*;

@RestController
@RequestMapping(RESUME)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    // private final FileUploadService fileUploadService;

    @PostMapping()
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request,
                                          Authentication authentication) {

        // Step 1: Call the service method
        Resume newResume = resumeService.createResume(request, authentication.getPrincipal());
        log.info("Resume created: {}", newResume);
        // Step 2: return response
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @GetMapping
    public ResponseEntity<?> getUserResumes(Authentication authentication) {
        // Step 1: call the service method
        List<Resume> resumes = resumeService.getUserResumes(authentication.getPrincipal());
        log.info("UserResumes: {}", resumes);
        // Step 2: return response
        return ResponseEntity.ok(resumes);

    }

    @GetMapping(ID)
    public ResponseEntity<?> getResumeById(@PathVariable String id, Authentication authentication) {
        // Step 1: Call the service method
        Resume existingResume = resumeService.getResumeById(id, authentication.getPrincipal());
        log.info("Resume found: {}", existingResume);
        // Step 2: return response
        return ResponseEntity.ok(existingResume);
    }

    @PutMapping(ID)
    public ResponseEntity<?> updateResume(@PathVariable String id,
                                          @RequestBody Resume updatedData,
                                          Authentication authentication) {
        // Step 1: Call the service method
        Resume updatedResume = resumeService.updateResume(id, updatedData, authentication.getPrincipal());
        log.info("Resume updated: {}", updatedResume);
        // Step 2: Return response
        return ResponseEntity.ok(updatedResume);
    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable String id,
                                          Authentication authentication) {

        // Step 1: Call the service method
        resumeService.deleteResume(id, authentication.getPrincipal());
        log.info("Resume deleted: {}", id);
        // Return the response
        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
    }

}
