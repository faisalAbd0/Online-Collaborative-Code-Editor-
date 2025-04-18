//package com.example.codeService.controller;
//
//
//import com.example.codeService.Dtos.CodeRequest;
//import com.example.codeService.service.CodeExecutionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/code")
//public class CodeExecutionController {
//    @Autowired
//    private CodeExecutionService codeExecutionService;
//
//    @PostMapping("/execute")
//    public String executeCode(@RequestBody CodeRequest request) {
//        try {
//            return codeExecutionService.executeCode(request.getCode(), request.getLanguage());
//
//        } catch (IOException e) {
//            return "Error executing code: " + e.getMessage();
//        }
//    }
//
//
//}
//
//
package com.example.codeService.controller;

import com.example.codeService.Dtos.CodeRequest;
import com.example.codeService.Dtos.MultiFileCodeRequest;
import com.example.codeService.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/code")
public class CodeExecutionController {
    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping("/execute")
    public String executeCode(@RequestBody CodeRequest request) {
        try {
            return codeExecutionService.executeCode(request.getCode(), request.getLanguage());
        } catch (IOException e) {
            return "Error executing code: " + e.getMessage();
        }
    }

    @PostMapping("/execute-project")
    public String executeMultiFileProject(@RequestBody MultiFileCodeRequest request) {
        try {
            return codeExecutionService.executeMultiFileProject(request.getFiles(), request.getLanguage());
        } catch (IOException e) {
            return "Error executing project: " + e.getMessage();
        }
    }
}