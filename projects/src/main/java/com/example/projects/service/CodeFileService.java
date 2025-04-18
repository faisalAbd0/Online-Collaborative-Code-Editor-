//package com.example.projects.service;
//
//import com.example.projects.models.CodeFile;
//import com.example.projects.models.CodeProject;
//import com.example.projects.repository.CodeFileRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CodeFileService {
//    private final CodeFileRepository codeFileRepository;
//
//    public List<CodeProject> findAll() {
//        return codeFileRepository.findAll();
//    }
//
//    public Optional<CodeProject> findById(String id) {
//        System.out.println(codeFileRepository.findById(id));
//        return codeFileRepository.findById(id);
//    }
//
//    public CodeProject save(CodeProject codeFile) {
//        return codeFileRepository.save(codeFile);
//    }
//
//    public Optional<CodeProject> addFileToProject(String projectId, CodeFile file) {
//        Optional<CodeProject> projectOpt = codeFileRepository.findById(projectId);
//
//        if (projectOpt.isPresent()) {
//            CodeProject project = projectOpt.get();
//
//            // Check if file with same name already exists
//            boolean fileExists = project.getFiles().stream()
//                    .anyMatch(existingFile -> existingFile.getFilename().equals(file.getFilename()));
//
//            if (!fileExists) {
//                // Set creation time
//                if (file.getCreatedAt() == null) {
//                    file.setCreatedAt(LocalDateTime.now());
//                }
//                if (file.getUpdatedAt() == null) {
//                    file.setUpdatedAt(LocalDateTime.now());
//                }
//
//                project.addFile(file);
//                return Optional.of(codeFileRepository.save(project));
//            }
//        }
//
//        return Optional.empty();
//    }
//
//    public Optional<CodeProject> updateFile(String projectId, String filename, String newContent) {
//        Optional<CodeProject> projectOpt = codeFileRepository.findById(projectId);
//
//        if (projectOpt.isPresent()) {
//            CodeProject project = projectOpt.get();
//
//            for (CodeFile file : project.getFiles()) {
//                if (file.getFilename().equals(filename)) {
//                    file.setContent(newContent);
//                    file.setUpdatedAt(LocalDateTime.now());
//                    return Optional.of(codeFileRepository.save(project));
//                }
//            }
//        }
//
//        return Optional.empty();
//    }
//
//    public boolean deleteFile(String projectId, String filename) {
//        Optional<CodeProject> projectOpt = codeFileRepository.findById(projectId);
//
//        if (projectOpt.isPresent()) {
//            CodeProject project = projectOpt.get();
//            boolean removed = project.getFiles().removeIf(file -> file.getFilename().equals(filename));
//
//            if (removed) {
//                codeFileRepository.save(project);
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public Optional<List<CodeProject>> findUserProject(Long id) {
//        System.out.println(codeFileRepository.findAllByUserId(id));
//        return codeFileRepository.findAllByUserId(id);
//    }
//}
package com.example.projects.service;

import com.example.projects.models.CodeFile;
import com.example.projects.models.CodeProject;
import com.example.projects.repository.CodeFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodeFileService {
    private final CodeFileRepository codeFileRepository;

    public List<CodeProject> findAll() {
        return codeFileRepository.findAll();
    }

    public Optional<CodeProject> findById(String id) {
        try {
            Long longId = Long.parseLong(id);
            return codeFileRepository.findById(longId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public CodeProject save(CodeProject codeFile) {
        return codeFileRepository.save(codeFile);
    }

    @Transactional
    public Optional<CodeProject> addFileToProject(String projectId, CodeFile file) {
        try {
            Long longId = Long.parseLong(projectId);
            Optional<CodeProject> projectOpt = codeFileRepository.findById(longId);

            if (projectOpt.isPresent()) {
                CodeProject project = projectOpt.get();

                // Check if file with same name already exists
                boolean fileExists = project.getFiles().stream()
                        .anyMatch(existingFile -> existingFile.getFilename().equals(file.getFilename()));

                if (!fileExists) {
                    // Set creation time
                    if (file.getCreatedAt() == null) {
                        file.setCreatedAt(LocalDateTime.now());
                    }
                    if (file.getUpdatedAt() == null) {
                        file.setUpdatedAt(LocalDateTime.now());
                    }

                    project.addFile(file);
                    return Optional.of(codeFileRepository.save(project));
                }
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<CodeProject> updateFile(String projectId, String filename, String newContent) {
        try {
            Long longId = Long.parseLong(projectId);
            Optional<CodeProject> projectOpt = codeFileRepository.findById(longId);

            if (projectOpt.isPresent()) {
                CodeProject project = projectOpt.get();

                for (CodeFile file : project.getFiles()) {
                    if (file.getFilename().equals(filename)) {
                        file.setContent(newContent);
                        file.setUpdatedAt(LocalDateTime.now());
                        return Optional.of(codeFileRepository.save(project));
                    }
                }
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteFile(String projectId, String filename) {
        try {
            Long longId = Long.parseLong(projectId);
            Optional<CodeProject> projectOpt = codeFileRepository.findById(longId);

            if (projectOpt.isPresent()) {
                CodeProject project = projectOpt.get();
                boolean removed = project.getFiles().removeIf(file -> file.getFilename().equals(filename));

                if (removed) {
                    codeFileRepository.save(project);
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }

    public Optional<List<CodeProject>> findUserProject(Long id) {
        return codeFileRepository.findAllByUserId(id);
    }
}