package com.example.demo.config;


import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UsersConfig {

    private final UserRepository userRepository;
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
//            User user = User.builder().email("admin@gmail.com").password("123")
//                    .firstName("faisal").lastName("abdo").username("SISI").build();
//
//            User user1 = User.builder().email("123@gmail.com").password("123")
//                    .firstName("momo").lastName("abdo").username("fafa").build();

            User user2 = new User();
            user2.setEmail("admin@gmail.com");
            user2.setPassword("123");
            user2.setFirstName("momo");
            user2.setLastName("abdo");
            user2.setUserIdentifier("fafa");

//            userRepository.save(user2);
//            userRepository.save(user1);

        };
    }
}
