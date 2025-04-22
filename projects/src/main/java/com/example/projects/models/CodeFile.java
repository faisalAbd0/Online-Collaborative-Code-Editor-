package com.example.projects.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "code_files")
public class CodeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ElementCollection
    private List<CodeVersion> history = new ArrayList<>();

    public CodeFile(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addVersion(String userId) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(new CodeVersion(content, userId, LocalDateTime.now(), "Auto snapshot"));
    }

}