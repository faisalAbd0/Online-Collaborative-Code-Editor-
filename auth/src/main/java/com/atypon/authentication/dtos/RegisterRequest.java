//package com.example.demo.Dtos;

package com.atypon.authentication.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String userIdentifier;
    private String password;

}
