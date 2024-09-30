package com.DemoKeyCloak.KeyCloak.Repository;

import com.DemoKeyCloak.KeyCloak.model.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {


    @Query("select u from UserOtp u where LOWER(u.keycloakUsername) = LOWER(?1) order by u.expirationTime DESC LIMIT 1")
    Optional<UserOtp> findByKeycloakUsername(String keycloakUsername);
}
