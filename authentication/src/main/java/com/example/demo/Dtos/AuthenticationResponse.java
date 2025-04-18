package com.example.demo.Dtos;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private String token;
}
