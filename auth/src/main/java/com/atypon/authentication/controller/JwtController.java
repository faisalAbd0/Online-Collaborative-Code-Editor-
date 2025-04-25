//package com.example.demo.controller;

package com.atypon.authentication.controller;
import com.atypon.authentication.dtos.AuthenticationRequest;
import com.atypon.authentication.dtos.AuthenticationResponse;
import com.atypon.authentication.dtos.TokenValidationRequest;
import com.atypon.authentication.dtos.TokenValidationResponse;
import com.atypon.authentication.serucity.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@CrossOrigin(origins =
        "http://localhost:3000")
public class JwtController {

    private final JwtService jwtService;

    @GetMapping("/isValid")
    public ResponseEntity<TokenValidationResponse> fetchIdByToken
            (@RequestHeader("Authorization") AuthenticationResponse authenticationRequest) {
        try {

            String authHeader = authenticationRequest.getToken();

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            System.out.println("JWT token received: " + token);


            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.ok(new TokenValidationResponse());
            }



            TokenValidationResponse tokenValidationResponse = new TokenValidationResponse();
            tokenValidationResponse.setUserId(jwtService.extractIdToken(token));
            return ResponseEntity.ok(tokenValidationResponse);
        } catch (Exception e) {
            System.err.println("Error processing token: " + e.getMessage());
            return ResponseEntity.status(500).body(new TokenValidationResponse());
        }
    }

}
