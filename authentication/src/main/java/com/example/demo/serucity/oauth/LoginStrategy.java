package com.example.demo.serucity.oauth;


import com.example.demo.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface LoginStrategy {
    User setInfo(User user , OAuth2User oauth2User);
}
