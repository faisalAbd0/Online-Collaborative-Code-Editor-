package com.example.codeService.Dtos;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeRequest {
    private String code;
    private String language;
}