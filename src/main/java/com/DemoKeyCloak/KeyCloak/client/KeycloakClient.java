package com.DemoKeyCloak.KeyCloak.client;


import com.DemoKeyCloak.KeyCloak.config.KeycloakClientConfiguration;
import com.DemoKeyCloak.KeyCloak.model.common.KeycloakAuthorizationRequest;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokAuthorizationAdminRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "keycloak-client", url = "${keycloak.server-url}", configuration = KeycloakClientConfiguration.class)
public interface KeycloakClient {

    @PostMapping(value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    AccessTokenResponse token(@RequestBody KeycloakAuthorizationRequest request);

    @PostMapping(value = "/realms/master/protocol/openid-connect/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    AccessTokenResponse adminToken(@RequestBody KeyclaokAuthorizationAdminRequest request);
}
