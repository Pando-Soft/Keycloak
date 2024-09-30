package com.DemoKeyCloak.KeyCloak.service.user.exception;

import com.DemoKeyCloak.KeyCloak.model.common.exception.KeyclaokRuntimeException;

public class AlreadyExistException extends KeyclaokRuntimeException {

    public AlreadyExistException(String message) {
        super(message);
    }
}
