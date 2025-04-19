//package com.example.demo.Dtos;
package com.atypon.authentication.dtos;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;

}
