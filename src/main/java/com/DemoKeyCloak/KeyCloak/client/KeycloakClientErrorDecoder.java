package com.DemoKeyCloak.KeyCloak.client;

import com.DemoKeyCloak.KeyCloak.model.error.KeycloakError;
import com.DemoKeyCloak.KeyCloak.model.exception.KeycloakException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeycloakClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        try {
            Response.Body body = response.body();
            if (body == null) {
                throw new KeycloakException("keycloakError");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            KeycloakError errorResponse = objectMapper.readValue(body.asInputStream(), KeycloakError.class);

            if (errorResponse.getError().equals("invalid_grant")) {
                throw new KeycloakException("invalidCredentials");
            }

            throw new KeycloakException(errorResponse.getErrorDescription());
        } catch (Exception e) {
            if (e instanceof KeycloakException keycloakException) {
                throw keycloakException;
            }
            log.error("Error occurred while parsing Keycloak error response body", e);
            throw new KeycloakException("keycloakError");
        }
    }
}
