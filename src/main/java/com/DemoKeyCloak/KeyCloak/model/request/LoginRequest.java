package com.DemoKeyCloak.KeyCloak.model.request;

import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.DemoKeyCloak.KeyCloak.model.common.exception.InvalidLoginCredentialException;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.enums.LoginTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.service.auth.validator.ValidPhoneValidator;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public record LoginRequest(

    String email,

    String password,

    @Nullable
    PhoneNumber phoneNumber,

    @Nullable
    LoginTypeEnum loginType,

    UserTypeEnum userType
) {
    public LoginRequest {
        if (loginType == LoginTypeEnum.EMAIL
            && StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidLoginCredentialException(KeyclaokCode.EMAIL_OR_PASSWORD_MISSING.getValue());
        }
        if (loginType == LoginTypeEnum.PHONE_NUMBER) {
            if (phoneNumber == null || !ValidPhoneValidator.validatePhoneNumber(phoneNumber))
                throw new ValidationException(KeyclaokCode.INVALID_PHONE_NUMBER.getValue());
        }
    }

}
