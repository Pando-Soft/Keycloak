package com.DemoKeyCloak.KeyCloak.model.enums;


public enum UserAccountRoleEnum {

    SUPER_ADMIN("SUPER_ADMIN"),
    SP_ADMIN("SP_ADMIN"),
    SP_WORKER("SP_WORKER"),
    SP_MANAGER("SP_MANAGER"),
    USER("USER");

    private final String value;

    private UserAccountRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
