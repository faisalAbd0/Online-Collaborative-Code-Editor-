package com.example.projects.dto;

import lombok.Data;

@Data
public class CodeEditMessage {
    private String projectId;
    private String filename;
    private String content;
    private Long userId;
    private String timestamp;
}
