package com.DemoKeyCloak.KeyCloak.model.request;

import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.DemoKeyCloak.KeyCloak.model.enums.LoginTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.service.auth.validator.ValidPhoneValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import org.apache.commons.lang3.StringUtils;

public record OtpLoginVerifyRequest(
    String email,

    @Nullable
    PhoneNumber phoneNumber,

    @NotBlank
    String otp,

    @NotNull
    LoginTypeEnum loginType,

    @NotNull
    UserTypeEnum userType
) {
    public OtpLoginVerifyRequest {
        if (loginType == LoginTypeEnum.EMAIL && (StringUtils.isEmpty(email))) {
            throw new BadRequestException("Email Missing");
        }

        if (loginType == LoginTypeEnum.PHONE_NUMBER)
            if (!ValidPhoneValidator.validatePhoneNumber(phoneNumber)) {
                throw new BadRequestException("Invalid Phone Number");
            }
    }
}
