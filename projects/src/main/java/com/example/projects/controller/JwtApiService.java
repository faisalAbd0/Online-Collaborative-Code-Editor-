package com.example.projects.controller;

import com.example.projects.dto.TokenValidationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Long callJwtApi(String token) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token); // OR .set("Authorization", "Bearer " + token);
        HttpEntity<TokenValidationResponse> entity = new HttpEntity<>(headers);

        System.out.println("Calling JWT API");
        ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                "http://backend:8080/api/jwt/isValid?token=" + token, // Add token as query param
                HttpMethod.GET,
                entity,
                TokenValidationResponse.class
        );
        System.out.println("Done calling JWT API");
        System.out.println("Resonspe.getStatus(): " + response.getStatusCode());
        System.out.println("Resonspe.getBody(): " + response.getBody());

        return response.getBody().getUserId();

    }


}
/*
@Service
@RequiredArgsConstructor
public class JwtApiService {

    private final RestTemplate restTemplate;

    public String callJwtApi(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // This is correct if you want to send as Authorization header
        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("Calling JWT API");
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/jwt/isValid?token=" + token, // Add token as query param
                HttpMethod.GET,
                entity,
                String.class
        );
        System.out.println("Done calling JWT API");
        System.out.println("Response.getStatus(): " + response.getStatusCode());
        System.out.println("Response.getBody(): " + response.getBody());

        return response.getBody();
    }
}
 */