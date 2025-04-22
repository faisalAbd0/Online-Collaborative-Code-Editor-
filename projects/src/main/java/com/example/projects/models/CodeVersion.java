package com.example.projects.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CodeVersion {
    private String content;
    private String editedBy; // or Long userId
    private LocalDateTime timestamp;
    private String message; // Optional: "Initial version", "Updated after fix", etc.
}
