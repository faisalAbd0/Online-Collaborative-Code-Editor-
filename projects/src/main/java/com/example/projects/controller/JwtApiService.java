package com.example.projects.controller;

import com.example.projects.dto.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@RestController
public class JwtApiService {

    private final RestTemplate restTemplate;
    public TokenValidationResponse getUserIdFromToken(String token) {
        // Method 1: Using query parameter as expected by the JwtController
        String url = "http://localhost:8080/api/jwt/isValid?token=" + token;
        ResponseEntity<TokenValidationResponse> response = restTemplate.getForEntity(url, TokenValidationResponse.class);

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("User ID from token: " + response.getBody());

        return response.getBody();
    }


    public String callJwtApi(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // OR .set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/jwt/isValid",
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println("Resonspe.getStatus(): " + response.getStatusCode());
        System.out.println("Resonspe.getBody(): " + response.getBody());

        return "DONE<DONE<DONE";

    }


}
