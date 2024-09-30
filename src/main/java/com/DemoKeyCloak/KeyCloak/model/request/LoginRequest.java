package com.DemoKeyCloak.KeyCloak.model.request;

import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.DemoKeyCloak.KeyCloak.model.enums.LoginTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.service.auth.validator.ValidPhoneValidator;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.BadRequestException;
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
            throw new BadRequestException("Email Or Password Missing");
        }
        if (loginType == LoginTypeEnum.PHONE_NUMBER) {
            if (phoneNumber == null || !ValidPhoneValidator.validatePhoneNumber(phoneNumber))
                throw new BadRequestException("Invalid Phone Number");
        }
    }

}
