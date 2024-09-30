package com.DemoKeyCloak.KeyCloak.model.enums;

public enum UserTypeEnum {

    ADMIN("admin"),
    USER("user");

    private final String value;

    private UserTypeEnum(String value) {
        this.value = value;
    }

    public Boolean isAdmin() {
        return this == ADMIN;
    }

    public Boolean isEmployee() {
        return this == USER;
    }

    public String getValue() {
        return this.value;
    }
}
