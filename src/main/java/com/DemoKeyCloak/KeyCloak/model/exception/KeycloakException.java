package com.DemoKeyCloak.KeyCloak.model.exception;

import com.DemoKeyCloak.KeyCloak.model.common.exception.KeyclaokRuntimeException;
import lombok.Getter;

@Getter
public class KeycloakException extends KeyclaokRuntimeException {

    public KeycloakException(String message) {
        super(message);
    }

}
