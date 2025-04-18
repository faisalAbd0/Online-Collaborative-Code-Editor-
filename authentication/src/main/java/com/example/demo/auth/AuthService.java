package com.example.demo.auth;


import com.example.demo.Dtos.AuthenticationRequest;
import com.example.demo.Dtos.AuthenticationResponse;
import com.example.demo.Dtos.RegisterRequest;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.serucity.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public Map<String ,Object> getClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());

        return claims;
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        System.out.println("@@@@@1");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        System.out.println("@@@@@2");

        User user = findUserByUsername(request.getEmail());

        String jwt = jwtService.generateToken(getClaims(user),user);
        System.out.println("@@@@@3");

        return AuthenticationResponse.builder().token(jwt).build();
//        return new AuthenticationResponse.AuthenticationResponseBuilder().token(jwt).build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        System.out.println("@@@@@@1");
        if (request.getUserIdentifier() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        System.out.println("@@@@@2");
        if (checkEmailExists(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        System.out.println("@@@@@3");


        User buildUser = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userIdentifier(request.getUserIdentifier())
                .email(request.getEmail()).build();

        userRepository.save(buildUser);



        User user = findUserByUsername(request.getEmail());
        String jwt = jwtService.generateToken(getClaims(user),user);
        System.out.println("@@@@@4");
        System.out.println(jwt);

        return AuthenticationResponse.builder().token(jwt).build();
//        return new AuthenticationResponse.AuthenticationResponseBuilder().token(jwt).build();

    }
    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user.get();
    }

    private boolean checkEmailExists(String email) {
        System.out.println("emailemailemail: " + email);
        return userRepository.existsByEmail(email);
    }

}
