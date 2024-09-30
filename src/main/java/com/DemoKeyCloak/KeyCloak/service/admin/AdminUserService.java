package com.DemoKeyCloak.KeyCloak.service.admin;

import com.DemoKeyCloak.KeyCloak.Repository.AdminUserRepository;
import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.exception.NotExistException;
import com.DemoKeyCloak.KeyCloak.model.entity.AdminUser;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.request.AdminLoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.LoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.service.keycloak.KeycloakService;
import com.DemoKeyCloak.KeyCloak.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final KeycloakService keycloakService;
    private final AdminUserRepository adminUserRepository;
    private final AuthService authService;
    private final MessageSource messageSource;

    public AccessTokenResponse adminLogin(AdminLoginRequest adminLoginRequest) {
        return keycloakService.login(adminLoginRequest.getUsername(), adminLoginRequest.getPassword());
    }

    public GenerateOtpDTO generateAdminLoginOtp(LoginRequest loginRequest) {
        String keycloakUsername = KeycloakService.buildKeycloakUsername(loginRequest.email(), UserTypeEnum.ADMIN);
        AdminUser user = adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername)
            .orElseThrow(() -> new NotExistException(KeyclaokCode.USER_DOSE_NOT_EXIST.getValue()));
        return authService.generateLoginOtp(user, loginRequest.password());
    }

    public AccessTokenResponse verifyAdminLoginOtp(OtpLoginVerifyRequest otpLoginVerifyRequest) {
        String keycloakUsername = KeycloakService.buildKeycloakUsername(otpLoginVerifyRequest.email(), UserTypeEnum.ADMIN);
        AdminUser user = adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername).
            orElseThrow(() -> new NotExistException(KeyclaokCode.USER_DOSE_NOT_EXIST.getValue()));
        return authService.verifyLoginOtp(otpLoginVerifyRequest, user);
    }
}
