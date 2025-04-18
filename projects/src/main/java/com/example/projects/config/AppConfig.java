package com.example.projects.config;

import com.example.projects.models.CodeProject;
import com.example.projects.models.Language;
import com.example.projects.repository.CodeFileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Configuration
public class AppConfig {

    @Bean
    CommandLineRunner commandLineRunner(CodeFileRepository codeFileRepository) {
        return args -> {
            // Only run this if the database is empty
            if (codeFileRepository.count() == 0) {
                CodeProject codeFile = new CodeProject(
                        "CodeEditor",
                        "Java",
                        Language.Java,
                        LocalDateTime.now(),
                        1L
                );
                CodeProject codeFile1 = new CodeProject(
                        "ML AI",
                        "Python",
                        Language.Python,
                        LocalDateTime.now(),
                        1L
                );

                CodeProject codeFile2 = new CodeProject(
                        "CodeEditor",
                        "Java",
                        Language.Java,
                        LocalDateTime.now(),
                        2L
                );
                CodeProject codeFile3 = new CodeProject(
                        "ML AI",
                        "Python",
                        Language.Python,
                        LocalDateTime.now(),
                        2L
                );

                CodeProject codeFile4 = new CodeProject(
                        "CodeEditor",
                        "Java",
                        Language.Java,
                        LocalDateTime.now(),
                        3L
                );
                CodeProject codeFile5 = new CodeProject(
                        "ML AI",
                        "Python",
                        Language.Python,
                        LocalDateTime.now(),
                        3L
                );

                codeFileRepository.save(codeFile);
                codeFileRepository.save(codeFile1);
                codeFileRepository.save(codeFile2);
                codeFileRepository.save(codeFile3);
                codeFileRepository.save(codeFile4);
                codeFileRepository.save(codeFile5);
            }
        };
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}