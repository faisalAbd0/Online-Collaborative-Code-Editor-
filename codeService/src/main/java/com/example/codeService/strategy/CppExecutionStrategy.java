package com.example.codeService.strategy;

import com.example.codeService.Dtos.MultiFileCodeRequest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.stream.Collectors;

@Component
public class CppExecutionStrategy implements ExecutionStrategy {
    @Override
    public boolean supports(String language) {
        return "cpp".equalsIgnoreCase(language);
    }

    @Override
    public String execute(MultiFileCodeRequest req) throws IOException {
        var entry = req.getFiles().entrySet().iterator().next();
        String filename = entry.getKey();
        File file = new File("/app/exec/" + filename);
        try(var writer = new FileWriter(file)) {
            writer.write(entry.getValue());
        }
        String cmd = String.format(
                "g++ /app/exec/%s -o /app/exec/a.out && /app/exec/a.out",
                filename
        );
        Process p = new ProcessBuilder(
                "docker","run","--rm","-v","/tmp/exec:/app/exec","gcc",
                "sh","-c",cmd
        ).redirectErrorStream(true).start();
        try(var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.lines().collect(Collectors.joining("\n"));
        } finally {
            file.delete();
        }
    }
}
