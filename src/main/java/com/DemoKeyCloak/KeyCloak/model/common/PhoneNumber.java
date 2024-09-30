package com.DemoKeyCloak.KeyCloak.model.common;

import jakarta.validation.constraints.NotNull;

public class PhoneNumber {

    @NotNull
    String code;

    @NotNull
    String number;

    public String getCode() {
        return this.code;
    }

    public String getNumber() {
        return this.number;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public PhoneNumber(final String code, final String number) {
        this.code = code;
        this.number = number;
    }
}
