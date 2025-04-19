//package com.example.projects.controller;
//
//import com.example.projects.models.CodeFile;
//import com.example.projects.models.CodeProject;
//import com.example.projects.service.CodeFileService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/code-file")
//@RequiredArgsConstructor
//public class CodeFileController {
//    final CodeFileService codeFileService;
//
//    @GetMapping
//    public List<CodeProject> getCodeFiles() {
//        return codeFileService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CodeProject> getProjectById(@PathVariable String id) {
//        return codeFileService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public CodeProject save(@RequestBody CodeProject codeFile) {
//        return codeFileService.save(codeFile);
//    }
//
//    @PostMapping("/{projectId}/files")
//    public ResponseEntity<CodeProject> addFile(
//            @PathVariable String projectId,
//            @RequestBody CodeFile file) {
//        return codeFileService.addFileToProject(projectId, file)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{projectId}/files/{filename}")
//    public ResponseEntity<CodeProject> updateFile(
//            @PathVariable String projectId,
//            @PathVariable String filename,
//            @RequestBody Map<String, String> update) {
//        return codeFileService.updateFile(projectId, filename, update.get("content"))
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{projectId}/files/{filename}")
//    public ResponseEntity<Void> deleteFile(
//            @PathVariable String projectId,
//            @PathVariable String filename) {
//        boolean deleted = codeFileService.deleteFile(projectId, filename);
//        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }
//}
package com.example.projects.controller;

import com.example.projects.dto.TokenValidationRequest;
import com.example.projects.dto.TokenValidationResponse;
import com.example.projects.models.CodeFile;
import com.example.projects.models.CodeProject;
import com.example.projects.service.CodeFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/code-file")
@RequiredArgsConstructor
@CrossOrigin
        (origins =
        "http://localhost:3000")
public class CodeFileController {
    final CodeFileService codeFileService;
    private final JwtApiService jwtApiService;



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
    @PostMapping
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
        return codeFileService.addFileToProject(projectId, file)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projectId}/files/{filename}")
    public ResponseEntity<CodeProject> updateFile(
            @PathVariable String projectId,
            @PathVariable String filename,
            @RequestBody Map<String, String> update) {
        return codeFileService.updateFile(projectId, filename, update.get("content"))
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

    public Long getId(String authHeader) {
        return jwtApiService.callJwtApi(authHeader);
    }

}