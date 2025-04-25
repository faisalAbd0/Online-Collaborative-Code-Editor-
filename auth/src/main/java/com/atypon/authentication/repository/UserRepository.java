//package com.example.demo.repository;

package com.atypon.authentication.repository;
import com.atypon.authentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {



    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean findByUserIdentifier(String userIdentifier);

    boolean existsByUserIdentifier(String userIdentifier);
}
