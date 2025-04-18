package com.example.projects.repository;

import com.example.projects.models.CodeProject;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface CodeFileRepository
        extends JpaRepository<CodeProject, Long> {

    Optional<CodeProject> findByUserId(Long userId);
    Optional<List<CodeProject>> findAllByUserId(Long userId);
}
