package com.example.demo.serucity.oauth;

import com.example.demo.models.User;
import com.example.demo.serucity.jwt.JwtService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;



@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2Token = getOAuth2Token(authentication);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        LoginStrategy loginStrategy = determineStrategy(oAuth2Token.getAuthorizedClientRegistrationId());
        User user = loginStrategy.setInfo(new User(), oAuth2User);

        registerUserIfNotExists(user);
        User userWithId = fetchUserFromDatabase(user.getEmail());

        String token = generateJwtToken(userWithId);
        redirectToClientWithToken(response, token);
    }

    private LoginStrategy determineStrategy(String registrationId) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleStrategy();
        } else if (registrationId.equalsIgnoreCase("github")) {
            return new GithubStrategy();
        }
        throw new IllegalStateException("Oauth Strategy not provided: " + registrationId);
    }

    private OAuth2AuthenticationToken getOAuth2Token(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            throw new IllegalStateException("Unexpected authentication type: " + authentication);
        }
        return token;
    }

    private void registerUserIfNotExists(User user) {
        if (!userService.findByIdentifier(user.getEmail())) {
            userService.save(user);
        }
    }

    private User fetchUserFromDatabase(String email) throws ServerException {
        return userService.findByEmail(email)
                .orElseThrow(() -> new ServerException("didn't stored in the database"));
    }

    private String generateJwtToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return jwtService.generateToken(claims, user);
    }

    private void redirectToClientWithToken(HttpServletResponse response, String token) throws IOException {
        String redirectUrl = "http://localhost:3000/home?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }

}
