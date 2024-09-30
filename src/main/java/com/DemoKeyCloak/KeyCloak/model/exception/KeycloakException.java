package com.DemoKeyCloak.KeyCloak.model.exception;

public class KeycloakException extends RuntimeException{
    private final int status;

    public KeycloakException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
