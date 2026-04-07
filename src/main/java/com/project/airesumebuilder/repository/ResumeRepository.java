package com.project.airesumebuilder.repository;

import com.project.airesumebuilder.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, String> {
    List<Resume> findByUserIdOrderByUpdatedAtDesc(Long UserId);
    Optional<Resume> findByUserIdAndId(Long UserId, String id);
}
