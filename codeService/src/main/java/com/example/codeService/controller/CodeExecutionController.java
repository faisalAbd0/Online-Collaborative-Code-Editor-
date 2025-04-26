package com.example.codeService.controller;

import com.example.codeService.Dtos.CodeRequest;
import com.example.codeService.Dtos.MultiFileCodeRequest;
import com.example.codeService.service.CodeExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
/*
public class CodeExecutionController {

    private CodeExecutionService codeExecutionService;

    @PostMapping("/execute")
    public String executeCode(@RequestBody CodeRequest request) {
        try {
            return codeExecutionService.executeCode(request.getCode(), request.getLanguage());
        } catch (IOException e) {
            return "Error executing code: " + e.getMessage();
        }
    }

    @PostMapping("/execute-multi")
    public String executeMulti(@RequestBody MultiFileCodeRequest req) {
        try {
            return codeExecutionService.executeMulti(req);
        } catch(IOException e) {
            return "Error: " + e.getMessage();
        }
    }


}

 */
public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

    @PostMapping("/execute")
    public String executeCode(@RequestBody CodeRequest req) throws IOException {
        MultiFileCodeRequest mreq=new MultiFileCodeRequest();
        mreq.setLanguage(req.getLanguage());
        mreq.setMainClass(req.getMainClass());
        mreq.setFiles(Map.of(req.getMainClass()+".java", req.getCode()));
        return codeExecutionService.execute(mreq);
    }

    @PostMapping("/execute-multi")
    public String executeMulti(@RequestBody MultiFileCodeRequest req) throws IOException {
        return codeExecutionService.execute(req);
    }
}
