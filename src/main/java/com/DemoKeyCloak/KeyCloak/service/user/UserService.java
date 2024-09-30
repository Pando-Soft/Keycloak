package com.DemoKeyCloak.KeyCloak.service.user;

import com.DemoKeyCloak.KeyCloak.model.request.UserLoginRequest;
import com.DemoKeyCloak.KeyCloak.service.keycloak.KeycloakService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;

    public AccessTokenResponse userLogin(UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getPhoneNumber().getNumber() == null) {
            throw new BadRequestException("phone number is required");
        }
        String fullPhoneNumber = userLoginRequest.getPhoneNumber().getCode() + userLoginRequest.getPhoneNumber().getNumber();
//        String keycloakUserName = keycloakService.getKeycloakUserName(fullPhoneNumber);
        return keycloakService.tokenExchange("test");
    }
}
