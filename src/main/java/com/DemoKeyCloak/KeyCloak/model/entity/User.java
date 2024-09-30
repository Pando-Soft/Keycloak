package com.DemoKeyCloak.KeyCloak.model.entity;

import com.DemoKeyCloak.KeyCloak.model.enums.UserStateEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "_user")
@Inheritance(strategy = InheritanceType.JOINED) // Specify JOINED strategy
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensures auto-increment
    private Long id;

    @Column(name = "keycloak_account_id")
    private String keycloakAccountId;

    @Column(unique = true, name = "keycloak_username")
    private String keycloakUsername;

    private String email;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "user_type") // Explicit mapping to avoid ambiguity
    private UserTypeEnum userType;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "user_state") // Explicit mapping
    private UserStateEnum userState;

    private String name;
}
