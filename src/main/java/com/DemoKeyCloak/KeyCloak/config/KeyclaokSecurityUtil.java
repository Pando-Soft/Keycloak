package com.DemoKeyCloak.KeyCloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyclaokSecurityUtil {

    Keycloak keycloak;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak-admin.username}")
    private String adminUsername;

    @Value("${keycloak-admin.password}")
    private String adminPassword;

    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(realm)
                .clientId("admin-cli")
                .username("test_admin")
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
        }
        return keycloak;
    }
}
