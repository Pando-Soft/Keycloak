package com.DemoKeyCloak.KeyCloak.controller;

import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.request.LoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.service.admin.AdminUserService;
import com.DemoKeyCloak.KeyCloak.service.user.EndUserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;
    private final EndUserService endUserService;

    @PostMapping("/login/otp")
    public ResponseEntity<GenerateOtpDTO> generatedLoginOtp(
        @RequestBody @Valid LoginRequest loginRequest,
        @RequestParam(value = "lang", required = false) Locale local
    ) {
        if (loginRequest.userType() == UserTypeEnum.USER) {
            return ResponseEntity.ok().body(endUserService.generateUserLoginOtp(loginRequest));
        } else {
            throw new BadRequestException(String.format("User loginType '%s' is not allowed to use OTP login", loginRequest.userType()));
        }
    }

    /*
    NOTE before verify-otp must open keyclaok admin cli and go to our realm and then open users,
    after that go to permission tab and choose user-impersonated scope and last thing you must to
    user you already created to policies in user-impersonated.
    after do this step you can check this endpoint and get the access token.
     */
    @PostMapping("/login/verify-otp")
    public ResponseEntity<AccessTokenResponse> verifyLoginOtp(
        @RequestBody @Valid OtpLoginVerifyRequest request,
        @RequestParam(value = "lang", required = false) Locale locale
    ) {
        if (request.userType() == UserTypeEnum.USER) {
            return ResponseEntity.ok(endUserService.verifyUserLoginOtp(request));
        } else {
            throw new ValidationException(String.format("User loginType '%s' is not allowed to use OTP login", request.userType()));
        }
    }

    @PostMapping("/login/password")
    public ResponseEntity<AccessTokenResponse> usernamePasswordLogin(
        @RequestBody @Valid LoginRequest loginRequest,
        @RequestParam(value = "lang", required = false) Locale locale
    ) {
        if (loginRequest.userType() == UserTypeEnum.ADMIN) {
            return ResponseEntity.ok(adminUserService.generateAdminLogin(loginRequest));
        } else {
            throw new ValidationException(String.format("User loginType '%s' is not allowed to use Password login", loginRequest.userType()));
        }
    }
}
