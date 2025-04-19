//package com.example.demo.Dtos;

package com.atypon.authentication.dtos;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private String token;
}
