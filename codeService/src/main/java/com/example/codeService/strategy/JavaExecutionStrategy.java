package com.example.codeService.strategy;

import com.example.codeService.Dtos.MultiFileCodeRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.stream.Collectors;


@Component
public class JavaExecutionStrategy implements ExecutionStrategy {
    @Override
    public boolean supports(String language) {
        return "java".equalsIgnoreCase(language);
    }

    @Override
    public String execute(MultiFileCodeRequest req) throws IOException {
        File dir = new File("/app/exec");
        for(File f: dir.listFiles()) f.delete();
        for(var entry : req.getFiles().entrySet()) {
            try(var writer = new FileWriter(new File(dir, entry.getKey()))) {
                writer.write(entry.getValue());
            }
        }

        String mainClass = req.getMainClass();
        String command = String.format(
                "javac /app/exec/*.java && java -cp /app/exec %s",
                mainClass
        );
        Process p = new ProcessBuilder("docker","run","--rm","-v","/tmp/exec:/app/exec",
                "openjdk","sh","-c",command)
                .redirectErrorStream(true)
                .start();
        try(var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

}
