package com.DemoKeyCloak.KeyCloak.service.admin;

import com.DemoKeyCloak.KeyCloak.Repository.AdminUserRepository;
import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.exception.NotExistException;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.entity.AdminUser;
import com.DemoKeyCloak.KeyCloak.model.entity.EndUser;
import com.DemoKeyCloak.KeyCloak.model.entity.User;
import com.DemoKeyCloak.KeyCloak.model.entity.UserOtp;
import com.DemoKeyCloak.KeyCloak.model.enums.UserAccountRoleEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserStateEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.request.LoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.service.auth.exception.NotActiveException;
import com.DemoKeyCloak.KeyCloak.service.keycloak.KeycloakService;
import com.DemoKeyCloak.KeyCloak.service.auth.AuthService;
import com.DemoKeyCloak.KeyCloak.service.user.exception.AlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AuthService authService;
    private final KeycloakService keycloakService;


    public AccessTokenResponse generateAdminLogin(LoginRequest loginRequest) {
        String keycloakUsername = loginRequest.email().substring(0, loginRequest.email().indexOf('@'));
        AdminUser user = adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername).orElse(null);
        if (user == null) {
            createAdminUser(loginRequest);

            // Get user again after registration
            user = adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername)
                .orElseThrow(() -> new NotExistException(KeyclaokCode.USER_DOSE_NOT_EXIST.getValue()));
        }
        return keycloakService.login(keycloakUsername, loginRequest.password());

    }

//    public AccessTokenResponse verifyAdminLoginOtp(OtpLoginVerifyRequest otpLoginVerifyRequest) {
//        String keycloakUsername = KeycloakService.buildKeycloakUsername(otpLoginVerifyRequest.email(), UserTypeEnum.ADMIN);
//        AdminUser user = adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername).
//            orElseThrow(() -> new NotExistException(KeyclaokCode.USER_DOSE_NOT_EXIST.getValue()));
//        return authService.verifyLoginOtp(otpLoginVerifyRequest, user);
//    }

    public void createAdminUser(LoginRequest loginRequest) {
        String keycloakUsername = loginRequest.email().substring(0, loginRequest.email().indexOf('@'));

        adminUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername).ifPresent(user -> {
            if (user.getUserState() == UserStateEnum.ACTIVE) {
                throw new AlreadyExistException(KeyclaokCode.USER_ALREADY_EXISTS.getValue());
            }
            throw new ValidationException(KeyclaokCode.UNABLE_TO_REGISTER_USER.getValue() + user.getUserState());
        });

        // Create keycloak admin user
        keycloakService.createAdminUserAccount(keycloakUsername, loginRequest.password(), loginRequest.email());
        String keycloakAccountId = keycloakService.getUserAdminRepresentation(keycloakUsername).getId();
        log.info("Created admin user Keycloak account ('{}').", keycloakUsername);

        // Create user
        AdminUser user = new AdminUser();
        user.setCountryCode(loginRequest.phoneNumber().getCode());
        user.setPhoneNumber(loginRequest.phoneNumber().getNumber());
        user.setKeycloakAccountId(keycloakAccountId);
        user.setKeycloakUsername(keycloakUsername);
        user.setUserState(UserStateEnum.ACTIVE);
        user.setUserType(UserTypeEnum.ADMIN);
        AdminUser createdUser = adminUserRepository.saveAndFlush(user);
        log.info("Created admin user ('{}').", keycloakUsername);

        // Assign roles to keycloak user
        keycloakService.assignRolesToUser(keycloakAccountId, Set.of(UserAccountRoleEnum.ADMIN));
        log.info("Assigned roles and attributes to end user Keycloak account ('{}').", keycloakUsername);
    }
}
