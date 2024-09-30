package com.DemoKeyCloak.KeyCloak.Repository;

import com.DemoKeyCloak.KeyCloak.model.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long>, JpaSpecificationExecutor<EndUser> {
    Optional<EndUser> findByKeycloakUsernameIgnoreCase(String keycloakUsername);

    Optional<EndUser> findByCountryCodeAndPhoneNumber(String countryCode, String phoneNumber);

    Optional<EndUser> findByNewsletterSubscriptionEmail(String email);
}
