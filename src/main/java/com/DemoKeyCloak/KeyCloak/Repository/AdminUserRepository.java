package com.DemoKeyCloak.KeyCloak.Repository;

import com.DemoKeyCloak.KeyCloak.model.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByKeycloakUsernameIgnoreCase(String keycloakUsername);
}
