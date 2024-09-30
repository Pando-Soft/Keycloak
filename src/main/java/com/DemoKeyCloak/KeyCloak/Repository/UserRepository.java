package com.DemoKeyCloak.KeyCloak.Repository;

import com.DemoKeyCloak.KeyCloak.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKeycloakUsernameIgnoreCase(String keycloakUsername);
}
