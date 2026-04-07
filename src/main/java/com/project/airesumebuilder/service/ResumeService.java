package com.project.airesumebuilder.service;

import com.project.airesumebuilder.dto.AuthResponse;
import com.project.airesumebuilder.dto.CreateResumeRequest;
import com.project.airesumebuilder.entity.Resume;
import com.project.airesumebuilder.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    public Resume createResume(CreateResumeRequest request, Object principal) {
        log.info("Create Resume request");
        // Step 1: Create a resume object
        Resume newResume = new Resume();

        // Step 2: Get the current profile
        AuthResponse response = authService.getProfile(principal);

        // Step 3: Update the resume object
        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());

        // Step 4: Set the default data for resume
        setDefaultResumeData(newResume);

        // Step 5: Save the resume data
        return resumeRepository.save(newResume);
    }

    private void setDefaultResumeData(Resume newResume) {
        log.info("Set default Resume data");
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperience(new ArrayList<>());
        newResume.setEducation(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }

    public List<Resume> getUserResumes(Object principal) {
        log.info("Get user resumes request");
        // Step 1: Get the current profile
        AuthResponse response = authService.getProfile(principal);

        // Step 2: Call the repository finder method
        List<Resume> resumes = resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());
        // Step 3: return result
        return resumes;

    }

    public Resume getResumeById(String resumeId, Object principal) {
        log.info("Get resume by id request");
        // Step 1: Get the current profile
        AuthResponse response = authService.getProfile(principal);

        // Step 2: call the repository finder method to get the resume details based on id
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // Step 3: return result
        return existingResume;

    }

    public Resume updateResume(String resumeId, Resume updatedData, Object principal) {
        log.info("Update resume by id request");
        // Step 1: get the current profile
        AuthResponse response = authService.getProfile(principal);

        // Step 2: Call the repository finder method
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // Step 3: Update the new data
        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplate(updatedData.getTemplate());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setContactInfo(updatedData.getContactInfo());
        existingResume.setWorkExperience(updatedData.getWorkExperience());
        existingResume.setEducation(updatedData.getEducation());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setLanguages(updatedData.getLanguages());
        existingResume.setInterests(updatedData.getInterests());

        // Step 4: Update the details into database
        resumeRepository.save(existingResume);

        // Step 5: Return result
        return existingResume;
    }

    public void deleteResume(String resumeId, Object principal) {
        log.info("Delete resume by id request");
        // Step 1: get the current profile
        AuthResponse response = authService.getProfile(principal);

        // Step 2: call repository finder method
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        resumeRepository.delete(existingResume);
    }
}
