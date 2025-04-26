//package com.example.demo.auth;

package com.atypon.authentication.auth;
import com.atypon.authentication.dtos.AuthenticationRequest;
import com.atypon.authentication.dtos.AuthenticationResponse;
import com.atypon.authentication.dtos.RegisterRequest;
import com.atypon.authentication.models.User;
import com.atypon.authentication.repository.UserRepository;
import com.atypon.authentication.serucity.jwt.JwtService;
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = findUserByUsername(request.getEmail());

        String jwt = jwtService.generateToken(getClaims(user),user);

        return AuthenticationResponse.builder().token(jwt).build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (request.getUserIdentifier() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        if (checkEmailExists(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        User buildUser = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userIdentifier(request.getUserIdentifier())
                .email(request.getEmail()).build();

        userRepository.save(buildUser);
        User user = findUserByUsername(request.getEmail());
        String jwt = jwtService.generateToken(getClaims(user),user);
        return AuthenticationResponse.builder().token(jwt).build();

    }
    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user.get();
    }

    private Map<String ,Object> getClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return claims;
    }

    private boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
