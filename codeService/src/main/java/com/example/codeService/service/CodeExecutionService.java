package com.example.codeService.service;

import com.example.codeService.Dtos.MultiFileCodeRequest;
import com.example.codeService.strategy.ExecutionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeExecutionService {
    private final List<ExecutionStrategy> strategies;

    public String execute(MultiFileCodeRequest req) throws IOException {
        return strategies.stream()
                .filter(s -> s.supports(req.getLanguage()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No executor for " + req.getLanguage()))
                .execute(req);
    }
}

