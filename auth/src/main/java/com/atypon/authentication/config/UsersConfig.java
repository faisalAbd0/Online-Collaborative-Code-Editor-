//package com.example.demo.config;

package com.atypon.authentication.config;

import com.atypon.authentication.models.User;
import com.atypon.authentication.repository.UserRepository;
import com.atypon.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UsersConfig {

    private final UserRepository userRepository;
    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {

            User u1 = new User();
            u1.setEmail("faisal111@gmail.com");
            u1.setPassword("123");
            u1.setFirstName("Momo");
            u1.setLastName("Abdo");
            u1.setUserIdentifier("fafa");
            userService.save(u1);

            // Second user
            User u2 = new User();
            u2.setEmail("issag@gmail.com");
            u2.setPassword("123");
            u2.setFirstName("Iss");
            u2.setLastName("Av");
            u2.setUserIdentifier("iso");
            userService.save(u2);

            // Third user
            User u3 = new User();
            u3.setEmail("ali@gmail.com");
            u3.setPassword("123");
            u3.setFirstName("Ali");
            u3.setLastName("Khan");
            u3.setUserIdentifier("ali123");
            userService.save(u3);


        };
    }
}
