package com.DemoKeyCloak.KeyCloak.model.common.exception;

public class KeyclaokRuntimeException extends RuntimeException {
    public KeyclaokRuntimeException() {
    }

    public KeyclaokRuntimeException(String message) {
        super(message);
    }

    public KeyclaokRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyclaokRuntimeException(Throwable cause) {
        super(cause);
    }

    public KeyclaokRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
