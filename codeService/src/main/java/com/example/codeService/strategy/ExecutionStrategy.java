package com.example.codeService.strategy;

import com.example.codeService.Dtos.MultiFileCodeRequest;

import java.io.IOException;

public interface ExecutionStrategy {
    boolean supports(String language);
    String execute(MultiFileCodeRequest request) throws IOException;
}
