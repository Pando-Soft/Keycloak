package com.DemoKeyCloak.KeyCloak.service.user;

import com.DemoKeyCloak.KeyCloak.Repository.EndUserRepository;
import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.DemoKeyCloak.KeyCloak.model.common.exception.NotExistException;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.entity.EndUser;
import com.DemoKeyCloak.KeyCloak.model.enums.UserAccountRoleEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserStateEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.request.LoginRequest;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.service.auth.AuthService;
import com.DemoKeyCloak.KeyCloak.service.keycloak.KeycloakService;
import com.DemoKeyCloak.KeyCloak.service.user.exception.AlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndUserService {

    private final AuthService authService;
    private final EndUserRepository endUserRepository;
    private final KeycloakService keycloakService;

    public GenerateOtpDTO generateUserLoginOtp(LoginRequest loginRequest) {
        if (loginRequest.phoneNumber() == null) {
            throw new ValidationException("Invalid Phone Number");
        }
        String fullPhoneNumber = loginRequest.phoneNumber().getCode() + loginRequest.phoneNumber().getNumber();
        String keycloakUsername = KeycloakService.buildKeycloakUsername(fullPhoneNumber, UserTypeEnum.USER);
        EndUser user = endUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername).orElse(null);

        // Register user if not exists
        if (user == null) {
            createEndUser(new PhoneNumber(loginRequest.phoneNumber().getCode(), loginRequest.phoneNumber().getNumber()));

            // Get user again after registration
            user = endUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername)
                .orElseThrow(() -> new NotExistException("User Does Not Exist"));
        }
        return authService.generateLoginOtp(user, loginRequest.password());
    }


    public void createEndUser(PhoneNumber phoneNumber) {
        String fullPhoneNumber = phoneNumber.getCode() + phoneNumber.getNumber();

        endUserRepository.findByCountryCodeAndPhoneNumber(phoneNumber.getCode(), phoneNumber.getNumber()).ifPresent(user -> {
            if (user.getUserState() == UserStateEnum.ACTIVE) {
                throw new AlreadyExistException("User Already Exists");
            }
            throw new ValidationException("Unable To Register User" + user.getUserState());
        });

        // Create keycloak user
        keycloakService.createUserAccount(fullPhoneNumber, null, UserTypeEnum.USER);
        String keycloakAccountId = keycloakService.getUserRepresentation(fullPhoneNumber, UserTypeEnum.USER).getId();
        log.info("Created end user Keycloak account ('{}').", fullPhoneNumber);

        // Create user
        EndUser user = new EndUser();
        user.setCountryCode(phoneNumber.getCode());
        user.setPhoneNumber(phoneNumber.getNumber());
        user.setKeycloakAccountId(keycloakAccountId);
        user.setKeycloakUsername(KeycloakService.buildKeycloakUsername(fullPhoneNumber, UserTypeEnum.USER));
        user.setUserState(UserStateEnum.ACTIVE);
        user.setUserType(UserTypeEnum.USER);
        EndUser createdUser = endUserRepository.saveAndFlush(user);
        log.info("Created end user ('{}').", fullPhoneNumber);

        // Assign roles and attributes to keycloak user
        keycloakService.assignRolesToUser(keycloakAccountId, Set.of(UserAccountRoleEnum.USER));
//        keycloakService.assignAttributesToUser(keycloakAccountId, new UserAttribute(createdUser.getId()));
        log.info("Assigned roles and attributes to end user Keycloak account ('{}').", fullPhoneNumber);
    }

    public AccessTokenResponse verifyUserLoginOtp(OtpLoginVerifyRequest otpLoginVerifyRequest) {
        if (otpLoginVerifyRequest.phoneNumber() == null) {
            throw new ValidationException("Invalid Phone Number");
        }

        String fullPhoneNumber = otpLoginVerifyRequest.phoneNumber().getCode() + otpLoginVerifyRequest.phoneNumber().getNumber();
        String keycloakUsername = KeycloakService.buildKeycloakUsername(fullPhoneNumber, UserTypeEnum.USER);
        EndUser user = endUserRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername)
            .orElseThrow(() -> new NotExistException("User Does Not Exist"));
        return authService.verifyLoginOtp(otpLoginVerifyRequest, user);
    }
}
