
package com.example.projects.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "code_projects")
@NoArgsConstructor
@ToString
public class CodeProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String description;

    @Enumerated(EnumType.STRING)
    private Language language;

    private LocalDateTime createdAt;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private List<CodeFile> files = new ArrayList<>();

    @ElementCollection
    private List<Long> collaboratorIds = new ArrayList<>();

    public void addCollaborator(Long userId) {
        if (!collaboratorIds.contains(userId)) {
            collaboratorIds.add(userId);
        }
    }

    public CodeProject(String projectName, String description, Language language,
                       LocalDateTime createdAt, Long userId) {
        this.projectName = projectName;
        this.description = description;
        this.language = language;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public CodeProject(String projectName, String description, Language language,
                       Long userId) {
        this.projectName = projectName;
        this.description = description;
        this.language = language;
        this.userId = userId;
    }

    public void addFile(CodeFile file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(file);
    }
}