package com.example.demo.serucity.oauth;

import ch.qos.logback.core.util.StringUtil;
import com.example.demo.models.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.stream.Stream;

public class GoogleStrategy implements LoginStrategy{

    @Override
    public User setInfo(User user, OAuth2User oauth2User) {
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String email = oauth2User.getAttribute("email");
        if (Stream.of(firstName, lastName, email).anyMatch(s -> s == null || s.trim().isEmpty())) {
            throw new IllegalArgumentException("Not valid credentials for Google OAuth");
        }

        String identity = email.substring(0, email.indexOf("@"));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserIdentifier(identity);
        user.setPassword("Google");
        return user;
    }
}
