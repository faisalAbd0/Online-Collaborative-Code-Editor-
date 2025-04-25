package com.atypon.authentication.service;
import com.atypon.authentication.models.User;
import com.atypon.authentication.repository.UserRepository;
import com.atypon.authentication.serucity.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;
    private final JwtService jwtService;
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(String token) {
        Long id = getIdToken(token);


        return userRepository.findById(id);
    }

    private Long getIdToken(String token) {
        return jwtService.extractIdToken(token.substring(7));
    }

    public void updateUserInfo(String token, Map<String, Object> info) {
        Long userId = getIdToken(token);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty())
            throw new IllegalStateException("User not found");

        User userInfo = user.get();
        if (info.containsKey("firstName")){
            userInfo.setFirstName(info.get("firstName").toString());
        }
        if (info.containsKey("lastName")){
            userInfo.setLastName(info.get("lastName").toString());
        }
        if (info.containsKey("userIdentifier")){
            userInfo.setUserIdentifier(info.get("userIdentifier").toString());
        }

        userRepository.save(userInfo);

    }

    public void save(User user) {
        userRepository.save(user);
    }
    public boolean findByIdentifier(String identifier) {
        return userRepository.existsByEmail(identifier) || userRepository.existsByUserIdentifier((identifier));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
