package com.example.projects.dto;

import lombok.Data;

@Data
public class AddCollaboratorRequest {
    private Long projectId;
    private Long collaboratorId;
}

