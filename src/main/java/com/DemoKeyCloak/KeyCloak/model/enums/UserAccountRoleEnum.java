package com.DemoKeyCloak.KeyCloak.model.enums;


public enum UserAccountRoleEnum {

    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    private UserAccountRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
