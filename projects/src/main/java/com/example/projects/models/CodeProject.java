////package com.example.projects.models;
////
////
////import lombok.Data;
////import org.springframework.data.annotation.Id;
////import org.springframework.data.mongodb.core.mapping.Document;
////
////import java.time.LocalDateTime;
////import java.util.ArrayList;
////import java.util.List;
////
////
////@Data
////@Document
////public class CodeProject {
////    @Id
////    private String id;
////    private String projectName;
////    private String description;
////    private Language language;
////    private LocalDateTime createdAt;
////    private Long userId;
////    private List<CodeFile> files = new ArrayList<>();
////
////
////    public CodeProject(String projectName, String description, Language language,
////                       LocalDateTime createdAt, Long userId) {
////        this.projectName = projectName;
////        this.description = description;
////        this.language = language;
////        this.createdAt = createdAt;
////        this.userId = userId;
////
////    }
////    public void addFile(CodeFile file) {
////        if (files == null) {
////            files = new ArrayList<>();
////        }
////        files.add(file);
////    }
////
////}
//package com.example.projects.models;
//
//import jakarta.annotation.Generated;
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@Document(collection = "codeFile") // Explicitly set collection name to match existing data
//public class CodeProject {
//    @Id
//    private String id;
//    private String projectName;
//    private String description;
//    private Language language;
//    private LocalDateTime createdAt;
//    private Long userId;
//    private List<CodeFile> files = new ArrayList<>();
//
//    public CodeProject() {
//    }
//
//    public CodeProject(String projectName, String description, Language language,
//                       LocalDateTime createdAt, Long userId) {
//        this.projectName = projectName;
//        this.description = description;
//        this.language = language;
//        this.createdAt = createdAt;
//        this.userId = userId;
//    }
//
//    public void addFile(CodeFile file) {
//        if (files == null) {
//            files = new ArrayList<>();
//        }
//        files.add(file);
//    }
//}
package com.example.projects.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "code_projects")
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

    public CodeProject() {
    }

    public CodeProject(String projectName, String description, Language language,
                       LocalDateTime createdAt, Long userId) {
        this.projectName = projectName;
        this.description = description;
        this.language = language;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public void addFile(CodeFile file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(file);
    }
}