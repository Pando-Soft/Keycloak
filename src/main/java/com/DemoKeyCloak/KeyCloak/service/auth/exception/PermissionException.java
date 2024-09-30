package com.DemoKeyCloak.KeyCloak.service.auth.exception;

import com.DemoKeyCloak.KeyCloak.model.common.exception.KeyclaokRuntimeException;

public class PermissionException extends KeyclaokRuntimeException {
    public PermissionException(String message) {
        super(message);
    }
}
