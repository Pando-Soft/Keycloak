package com.DemoKeyCloak.KeyCloak.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_username")
    private String keycloakUsername;

    private String otp;

    @Column(name = "expiration_time")
    private ZonedDateTime expirationTime;
}
