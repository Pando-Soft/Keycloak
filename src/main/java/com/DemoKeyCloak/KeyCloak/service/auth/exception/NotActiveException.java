package com.DemoKeyCloak.KeyCloak.service.auth.exception;

import com.DemoKeyCloak.KeyCloak.model.common.exception.KeyclaokRuntimeException;

public class NotActiveException extends KeyclaokRuntimeException {
    public NotActiveException(String message) {
        super(message);
    }
}
