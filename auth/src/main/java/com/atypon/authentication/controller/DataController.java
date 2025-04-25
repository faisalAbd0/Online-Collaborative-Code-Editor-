//package com.example.demo.controller;

package com.atypon.authentication.controller;
import com.atypon.authentication.models.User;
import com.atypon.authentication.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final UserService userService;
    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /** Given a list of IDs, return their User objects */
    @PostMapping("/users/batch")
    public List<User> getUsersByIds(@RequestBody List<Long> ids) {
        return userService.findByIds(ids);
    }

    @GetMapping("/user-info")
    public User getUserInfo(@RequestHeader("Authorization")  String token) {

        Optional<User> user =  userService.findUser(token);
        if (user.isEmpty())
            throw new IllegalStateException("User not found");

        return user.get();
    }

    @GetMapping("/info")
    public ResponseEntity<? extends Map<String,? extends Object>> getInfo
            (@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return ResponseEntity.ok(principal.getAttributes());

    }
    @PutMapping("/edit-info")
    public void editUserInfo(@RequestHeader("Authorization")  String token,
                             @RequestBody  Map<String, Object> info) {
        userService.updateUserInfo(token, info);
    }


}
