package com.DemoKeyCloak.KeyCloak.service.auth;

import com.DemoKeyCloak.KeyCloak.Repository.UserOtpRepository;
import com.DemoKeyCloak.KeyCloak.Repository.UserRepository;
import com.DemoKeyCloak.KeyCloak.model.common.GenerateOtpDTO;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.exception.NotExistException;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import com.DemoKeyCloak.KeyCloak.model.entity.User;
import com.DemoKeyCloak.KeyCloak.model.entity.UserOtp;
import com.DemoKeyCloak.KeyCloak.model.enums.UserStateEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.request.OtpLoginVerifyRequest;
import com.DemoKeyCloak.KeyCloak.service.auth.exception.NotActiveException;
import com.DemoKeyCloak.KeyCloak.service.auth.exception.PermissionException;
import com.DemoKeyCloak.KeyCloak.service.keycloak.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.util.SecretGenerator;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserOtpRepository userOtpRepository;
    private final KeycloakService keyclaokService;
    private final UserRepository userRepository;


    public UserOtp createUserOtp(String keycloakUsername) {
        String otp = SecretGenerator.getInstance().randomString(4, SecretGenerator.DIGITS);
        UserOtp userOtp = new UserOtp();
        userOtp.setOtp(otp);
        userOtp.setKeycloakUsername(keycloakUsername);
        userOtp.setExpirationTime(ZonedDateTime.now().plusMinutes(5));
        return userOtp;
    }

    public GenerateOtpDTO generateLoginOtp(User user, String password) {
        if (user.getUserState() != UserStateEnum.ACTIVE) {
            throw new NotActiveException(KeyclaokCode.USER_INACTIVE.getValue() + user.getUserState());
        }

        String keycloakUsername = user.getKeycloakUsername();
        userOtpRepository.findByKeycloakUsername(keycloakUsername).ifPresent(userOtp -> {
            if (ZonedDateTime.now().isBefore(userOtp.getExpirationTime())) {
                var expiresInSeconds = userOtp.getExpirationTime().toEpochSecond() - ZonedDateTime.now().toEpochSecond();
                var msg = "OTP already sent. Please wait for the OTP to expire before requesting a new one. Expires in " + expiresInSeconds + " seconds.";
                log.warn(msg);
                throw new ValidationException(KeyclaokCode.OTP_ALREADY_SENT.getValue() + expiresInSeconds);
            }
        });

        UserOtp userOtp = createUserOtp(keycloakUsername);

        if (user.getUserType() == UserTypeEnum.USER) {
            log.info("OTP {} sent to user {} via SMS", userOtp.getOtp(), user.getCountryCode().concat(user.getPhoneNumber()));


        } else if (user.getUserType() == UserTypeEnum.ADMIN) {
            // Use keycloak login to validate credentials only.
            // Token is not returned to user yet as OTP is required to be validated first.
            keyclaokService.login(user.getKeycloakUsername(), password);
            log.info("OTP {} sent to user {} via email", userOtp.getOtp(), user.getEmail());
        } else {
            throw new ValidationException(KeyclaokCode.OTP_LOGIN_NOT_SUPPORTED.getValue() + user.getUserType());
        }

        userOtpRepository.saveAndFlush(userOtp);

        return new GenerateOtpDTO(userOtp.getExpirationTime());


    }

    public AccessTokenResponse verifyLoginOtp(OtpLoginVerifyRequest otpLoginVerifyRequest, User user) {
        if (user.getUserState() != UserStateEnum.ACTIVE) {
            throw new NotActiveException(KeyclaokCode.USER_INACTIVE.getValue() + user.getUserState());
        }

        String keycloakUsername = user.getKeycloakUsername();

        UserOtp userOtp = userOtpRepository.findByKeycloakUsername(keycloakUsername).orElseThrow(
            () -> new NotExistException(KeyclaokCode.OTP_NOT_FOUND.getValue())
        );

        boolean isOtpExpired = ZonedDateTime.now().isAfter(userOtp.getExpirationTime());
        boolean isOtpValid = userOtp.getOtp().equals(otpLoginVerifyRequest.otp());
        if (isOtpExpired) throw new PermissionException(KeyclaokCode.OTP_EXPIRED.getValue());
        if (!isOtpValid) throw new PermissionException(KeyclaokCode.OTP_INVALID.getValue());

        userRepository.findByKeycloakUsernameIgnoreCase(keycloakUsername)
            .orElseThrow(() -> new NotExistException(KeyclaokCode.USER_DOSE_NOT_EXIST.getValue()));

        // Remove OTP after successful validation
        userOtpRepository.delete(userOtp);

        return keyclaokService.tokenExchange(keycloakUsername);
    }
}
