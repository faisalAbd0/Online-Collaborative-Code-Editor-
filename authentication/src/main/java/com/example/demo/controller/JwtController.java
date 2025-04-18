package com.example.demo.controller;


import com.example.demo.Dtos.TokenValidationResponse;
import com.example.demo.serucity.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class JwtController {

    private final JwtService jwtService;


//    @GetMapping("/isValid")
//    public ResponseEntity<Long> fetchIdByToken1(@RequestParam String token) {
//        System.out.println("HIHIHTITHITTI");
//        System.out.println(token);
//        if (jwtService.isTokenExpired(token)){
//            ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok().body(jwtService.extractIdToken(token.substring(7)));
//    }

    @GetMapping("/isValid")
    public ResponseEntity<TokenValidationResponse> fetchIdByToken(String token) {
        System.out.println("Token received: " + token);

        if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.noContent().build();
        }

        Long userId = jwtService.extractIdToken(token);
        return ResponseEntity.ok(new TokenValidationResponse(userId));
    }
}
