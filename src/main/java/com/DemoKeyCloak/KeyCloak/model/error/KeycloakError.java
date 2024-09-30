package com.DemoKeyCloak.KeyCloak.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KeycloakError {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}

