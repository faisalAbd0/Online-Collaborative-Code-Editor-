package com.example.codeService.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class MultiFileCodeRequest {
    private Map<String,String> files;
    private String language;
    private String mainClass;
}
