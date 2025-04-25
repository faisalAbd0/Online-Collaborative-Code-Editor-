package com.example.codeService.service;

import com.example.codeService.Dtos.MultiFileCodeRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CodeExecutionService {
    public String executeCode(String code, String language) throws IOException {
        String result;

        String filename = "code." + getFileExtension(language);
        File file = new File("/app/exec/" + filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        }

        List<String> dockerCommand = getDockerCommand(language, filename);
        System.out.println("DockerCommand: " + dockerCommand);

        ProcessBuilder processBuilder = new ProcessBuilder(dockerCommand);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            result = output.toString();
        }

        file.delete();
        return result;
    }


    private String getFileExtension(String language) {
        switch (language) {
            case "java": return "java";
            case "python": return "py";
            case "c": return "c";
            case "cpp": return "cpp";
            default: throw new IllegalArgumentException("Unsupported language");
        }
    }

    private List<String> getDockerCommand(String language, String filename) {
        String volumeMount = "/tmp/exec:/app/exec";

        switch (language) {
            case "java":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "openjdk",
                        "sh", "-c",
                        "javac /app/exec/" + filename + " && java -cp /app/exec Main; rm -f /app/exec/Main.class"
                );
            case "python":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "python",
                        "python", "/app/exec/" + filename
                );
            case "c":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "gcc",
                        "sh", "-c",
                        "gcc /app/exec/" + filename + " -o /app/exec/a.out && /app/exec/a.out; rm -f /app/exec/a.out"
                );
            case "cpp":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "gcc",
                        "sh", "-c",
                        "g++ /app/exec/" + filename + " -o /app/exec/a.out && /app/exec/a.out; rm -f /app/exec/a.out"
                );
            default:
                throw new IllegalArgumentException("Unsupported language");
        }
    }

    public String executeMulti(MultiFileCodeRequest req) throws IOException {
        File dir = new File("/app/exec");
        // clean directory
        for(File f: dir.listFiles()) f.delete();
        // write every file
        for(var entry : req.getFiles().entrySet()) {
            File out = new File(dir, entry.getKey());
            try (var w = new FileWriter(out)) {
                w.write(entry.getValue());
            }
        }

        List<String> cmd;
        if ("java".equalsIgnoreCase(req.getLanguage())) {
            // compile all .java, then run the main class
            cmd = List.of(
                    "docker","run","--rm",
                    "-v","/tmp/exec:/app/exec",
                    "openjdk",
                    "sh","-c",
                    "javac /app/exec/*.java && java -cp /app/exec " + req.getMainClass()
            );
        } else {
            // fall back to single-file logic for Python/C/C++
            cmd = getDockerCommand(req.getLanguage(), req.getFiles().keySet().iterator().next());
        }

        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

}