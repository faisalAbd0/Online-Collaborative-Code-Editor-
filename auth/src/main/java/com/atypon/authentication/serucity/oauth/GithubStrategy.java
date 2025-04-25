//package com.example.demo.serucity.oauth;
package com.atypon.authentication.serucity.oauth;
import com.atypon.authentication.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.stream.Stream;

public class GithubStrategy implements LoginStrategy{
    @Override
    public User setInfo(User user, OAuth2User oauth2User) {

        String login = oauth2User.getAttribute("login");
        String fullName = oauth2User.getAttribute("name");

        if (Stream.of(login, fullName).anyMatch(s -> s == null || s.trim().isEmpty())) {
            throw new IllegalArgumentException("Not valid credentials for Google OAuth");
        }
        String email = login + "@github.com";

        user.setEmail(email);
        user.setUserIdentifier(login);
        user.setFirstName(fullName);
        user.setLastName(fullName);
        user.setPassword("Github");

        return user;
    }
}
