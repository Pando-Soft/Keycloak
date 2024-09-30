package com.DemoKeyCloak.KeyCloak.model.common;

import lombok.Getter;

public enum KeyclaokCode {

    USER_DOSE_NOT_EXIST("user.dose.not.exist"),
    USER_INACTIVE("user.inactive"),
    OTP_ALREADY_SENT("otp.already.sent"),
    OTP_LOGIN_NOT_SUPPORTED("otp.login.not.supported"),
    OTP_NOT_FOUND("otp.not.found"),
    OTP_EXPIRED("otp.expired"),
    OTP_INVALID("otp.invalid"),
    KEYCLAOK_USER_ERROR("keycloak.user.error"),
    CLIENT_NOT_FOUND("client.not.found"),
    INVALID_PHONE__NUMBER("invalid.phone.number"),
    USER_ALREADY_EXISTS("user.already.exists"),
    UNABLE_TO_REGISTER_USER("unable.to.register.user");

    @Getter
    private final String value;

    KeyclaokCode(String value) {
        this.value = value;
    }
}
