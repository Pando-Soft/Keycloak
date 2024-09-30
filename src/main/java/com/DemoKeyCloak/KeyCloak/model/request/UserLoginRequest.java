package com.DemoKeyCloak.KeyCloak.model.request;

import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.DemoKeyCloak.KeyCloak.model.enums.LoginTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotNull
    @NotBlank
    PhoneNumber phoneNumber;

    @NotNull
    @NotBlank
    LoginTypeEnum loginType;

    UserTypeEnum userType;


}
