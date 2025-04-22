package com.example.codeService.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

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


    public String executeMultiFileProject(Map<String, String> files, String language) throws IOException {
        String result;
        String execDir = "/app/exec/project_" + System.currentTimeMillis();
        File projectDir = new File(execDir);
        projectDir.mkdirs();

        // Write all files to the project directory
        for (Map.Entry<String, String> entry : files.entrySet()) {
            File file = new File(projectDir, entry.getKey());
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(entry.getValue());
            }
        }

        // Determine main file name based on language
        String mainFileName = determineMainFile(files.keySet(), language);
        if (mainFileName == null) {
            return "Error: Could not determine main file for execution";
        }

        List<String> dockerCommand = getMultiFileDockerCommand(language, execDir, mainFileName);
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

        // Clean up
        deleteDirectory(projectDir);
        return result;
    }

    private String determineMainFile(java.util.Set<String> filenames, String language) {
        // Default heuristic: look for files that might contain main methods or entry points
        switch (language) {
            case "java":
                // Look for a file with "Main" in the name
                for (String filename : filenames) {
                    if (filename.contains("Main") && filename.endsWith(".java")) {
                        return filename;
                    }
                }
                // If no Main file found, just use the first Java file
                for (String filename : filenames) {
                    if (filename.endsWith(".java")) {
                        return filename;
                    }
                }
                break;
            case "python":
                // Look for a file with "main" in the name
                for (String filename : filenames) {
                    if (filename.contains("main") && filename.endsWith(".py")) {
                        return filename;
                    }
                }
                // If no main file found, just use the first Python file
                for (String filename : filenames) {
                    if (filename.endsWith(".py")) {
                        return filename;
                    }
                }
                break;
            case "c":
            case "cpp":
                // Look for a file with "main" in the name
                for (String filename : filenames) {
                    if (filename.contains("main") &&
                            (filename.endsWith(".c") || filename.endsWith(".cpp"))) {
                        return filename;
                    }
                }
                // If no main file found, just use the first C/C++ file
                for (String filename : filenames) {
                    if (filename.endsWith(".c") || filename.endsWith(".cpp")) {
                        return filename;
                    }
                }
                break;
        }
        return null;
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }


    private List<String> getMultiFileDockerCommand(String language, String projectDir, String mainFileName) {
        String relativeProjectPath = projectDir.substring(projectDir.lastIndexOf("/") + 1);
        String volumeMount = "/tmp/exec:/app/exec";

        switch (language) {
            case "java":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "openjdk",
                        "sh", "-c",
                        "cd /app/exec/" + relativeProjectPath + " && " +
                                "javac *.java && " +
                                "java -cp . " + mainFileName.replace(".java", "") + "; " +
                                "rm -f *.class"
                );
            case "python":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "python",
                        "sh", "-c",
                        "cd /app/exec/" + relativeProjectPath + " && " +
                                "python " + mainFileName
                );
            case "c":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "gcc",
                        "sh", "-c",
                        "cd /app/exec/" + relativeProjectPath + " && " +
                                "gcc *.c -o a.out && ./a.out; rm -f a.out"
                );
            case "cpp":
                return List.of(
                        "docker", "run", "--rm", "-v", volumeMount, "gcc",
                        "sh", "-c",
                        "cd /app/exec/" + relativeProjectPath + " && " +
                                "g++ *.cpp -o a.out && ./a.out; rm -f a.out"
                );
            default:
                throw new IllegalArgumentException("Unsupported language");
        }
    }
}