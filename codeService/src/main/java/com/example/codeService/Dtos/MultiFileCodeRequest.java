package com.example.codeService.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MultiFileCodeRequest {
    private Map<String, String> files; // filename -> content
    private String language;
}