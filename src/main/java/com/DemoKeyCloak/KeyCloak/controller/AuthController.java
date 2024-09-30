package com.DemoKeyCloak.KeyCloak.controller;

import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.request.AdminLoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.LoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.model.request.UserLoginRequest;
import com.DemoKeyCloak.KeyCloak.service.admin.AdminUserService;
import com.DemoKeyCloak.KeyCloak.service.user.EndUserService;
import com.DemoKeyCloak.KeyCloak.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;
    private final UserService userService;
    private final EndUserService endUserService;

    @PostMapping("/login/otp")
    public ResponseEntity<GenerateOtpDTO> generatedLoginOtp(@RequestBody @Valid LoginRequest loginRequest) {
        return switch (loginRequest.userType()) {
            case ADMIN -> ResponseEntity.ok().body(adminUserService.generateAdminLoginOtp(loginRequest));
            case USER -> ResponseEntity.ok().body(endUserService.generateUserLoginOtp(loginRequest));
            default ->
                throw new BadRequestException(String.format("User loginType '%s' is not allowed to use OTP login", loginRequest.userType()));
        };
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<AccessTokenResponse> verifyLoginOtp(@RequestBody @Valid OtpLoginVerifyRequest request) {
        return switch (request.userType()) {
            case ADMIN -> ResponseEntity.ok(adminUserService.verifyAdminLoginOtp(request));
            case USER -> ResponseEntity.ok(endUserService.verifyUserLoginOtp(request));
            default ->
                throw new ValidationException(String.format("User loginType '%s' is not allowed to use OTP login", request.userType()));
        };
    }


    @PostMapping("/login/test/password")
    public ResponseEntity<AccessTokenResponse> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {
        AccessTokenResponse body = adminUserService.adminLogin(adminLoginRequest);
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/login/test/otp")
    private ResponseEntity<AccessTokenResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        AccessTokenResponse body = userService.userLogin(userLoginRequest);
        return ResponseEntity.ok().body(body);
    }


}
