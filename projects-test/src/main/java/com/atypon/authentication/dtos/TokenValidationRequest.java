package com.atypon.authentication.dtos;


import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
}
