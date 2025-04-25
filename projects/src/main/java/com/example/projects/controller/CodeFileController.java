package com.example.projects.controller;

import com.example.projects.dto.AddCollaboratorRequest;
import com.example.projects.dto.TokenValidationRequest;
import com.example.projects.dto.TokenValidationResponse;
import com.example.projects.models.CodeFile;
import com.example.projects.models.CodeProject;
import com.example.projects.models.CodeVersion;
import com.example.projects.service.CodeFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/code-file")
@RequiredArgsConstructor
@CrossOrigin
        (origins =
        "http://localhost:3000")
public class CodeFileController {
    final CodeFileService codeFileService;

    @PostMapping("/user-projects")
    public List<CodeProject> findProjectsByToken(
            @RequestBody TokenValidationResponse tokenValidationResponse
    ) {

        Long userId = tokenValidationResponse.getUserId();
        System.out.println(">>>>>>>");
        System.out.println(userId);
        System.out.println("<<<<<<<");
        Optional<List<CodeProject>> code = codeFileService.findUserProject(userId);
        if (code.isEmpty()) {
            return new ArrayList<>();
        }
        return code.get();
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<CodeProject> findProjectById(
            @PathVariable String id
    ){
        Optional<CodeProject> project =codeFileService.findById(id);
        if (project.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project.get());
    }
    @PostMapping("/save")
    public ResponseEntity<CodeProject> save(@RequestBody CodeProject codeProject) {
        // Ensure we have a created date
        if (codeProject.getCreatedAt() == null) {
            codeProject.setCreatedAt(java.time.LocalDateTime.now());
        }

        CodeProject savedProject = codeFileService.save(codeProject);
        return ResponseEntity.ok(savedProject);
    }

    @PostMapping("/{projectId}/files")
    public ResponseEntity<CodeProject> addFile(
            @PathVariable String projectId,
            @RequestBody CodeFile file) {
        System.out.println(projectId);
        System.out.println(file);
        return codeFileService.addFileToProject(projectId, file)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projectId}/files/{filename}")
    public ResponseEntity<CodeProject> updateFile(
            @PathVariable String projectId,
            @PathVariable String filename,
            @RequestBody Map<String, String> update
    ) {


        String content = update.get("content");
        String userId = update.get("userId"); // 👈 userId is passed directly from the frontend

        System.out.println("Updating file for userId: " + userId);
        System.out.println("The content is: " + content);
        return codeFileService.updateFile(projectId, filename, content, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{projectId}/files/{filename}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String projectId,
            @PathVariable String filename) {
        boolean deleted = codeFileService.deleteFile(projectId, filename);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


    @GetMapping("/{projectId}/files/{filename}/history")
    public ResponseEntity<List<CodeVersion>> getFileHistory(
            @PathVariable String projectId,
            @PathVariable String filename
    ) {
        Optional<CodeProject> projectOpt = codeFileService.findById(projectId);
        if (projectOpt.isPresent()) {
            for (CodeFile file : projectOpt.get().getFiles()) {
                if (file.getFilename().equals(filename)) {
                    return ResponseEntity.ok(file.getHistory());
                }
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/add-collaborator")
    public ResponseEntity<?> addCollaborator(@RequestBody AddCollaboratorRequest request) {
        Optional<CodeProject> projectOpt = codeFileService.findById(String.valueOf(request.getProjectId()));
        if (projectOpt.isEmpty()) return ResponseEntity.notFound().build();

        CodeProject project = projectOpt.get();
        project.addCollaborator(request.getCollaboratorId());
        codeFileService.save(project);

        return ResponseEntity.ok("Collaborator added");
    }

    @GetMapping("/{projectId}/collaborators")
    public ResponseEntity<List<Long>> getCollaborators(@PathVariable String projectId) {
        Optional<CodeProject> projectOpt = codeFileService.findById(projectId);
        if (projectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CodeProject project = projectOpt.get();
        return ResponseEntity.ok(project.getCollaboratorIds());
    }

    @PostMapping("/shared-projects")
    public ResponseEntity<List<CodeProject>> getSharedProjects(
            @RequestBody TokenValidationResponse tokenValidation
    ) {
        Long userId = tokenValidation.getUserId();
        return codeFileService
                .findProjectsByCollaborator(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(Collections.emptyList()));
    }





}
