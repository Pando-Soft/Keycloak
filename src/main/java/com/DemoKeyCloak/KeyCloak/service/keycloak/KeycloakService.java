package com.DemoKeyCloak.KeyCloak.service.keycloak;

import com.DemoKeyCloak.KeyCloak.client.KeycloakClient;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokAuthorizationAdminRequest;
import com.DemoKeyCloak.KeyCloak.model.common.KeycloakAuthorizationRequest;
import com.DemoKeyCloak.KeyCloak.model.common.UserAttribute;
import com.DemoKeyCloak.KeyCloak.model.enums.UserAccountRoleEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.exception.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.DemoKeyCloak.KeyCloak.config.KeyclaokSecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String GRANT_TYPE_EXCHANGE_TOKEN = "urn:ietf:params:oauth:grant-type:token-exchange";
    private static final String GRANT_TYPE_PASSWORD = "password";

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final KeycloakClient keycloakClient;
    private final KeyclaokSecurityUtil keycloak;


//    public String getKeycloakUserName(String fullPhoneNumber) {
//        UsersResource usersResource = keyclaok.realm(realm).users();
//
//        // Pagination parameters
//        int first = 0;
//        int maxResults = 100; // Adjust as needed
//        List<UserRepresentation> users;
//
//        do {
//            // Fetch a batch of users
//            users = usersResource.search(null, null, null, null, first, maxResults);
//            for (UserRepresentation user : users) {
//                if (user.getAttributes() != null && user.getAttributes().containsKey("phone_number")) {
//                    List<String> phoneNumbers = user.getAttributes().get("phone_number");
//                    if (phoneNumbers != null && phoneNumbers.contains(fullPhoneNumber)) {
//                        return user.getUsername();
//                    }
//                }
//            }
//            first += maxResults;
//        } while (!users.isEmpty());
//
//        return null; // or throw an exception if user is not found
//
//
//    }

    public static String buildKeycloakUsername(final String username, final UserTypeEnum userType) {
        return userType.getValue() + "-" + username;
    }

    public AccessTokenResponse tokenExchange(String keycloakUsername) {
        return keycloakClient.token(KeycloakAuthorizationRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .grant_type(GRANT_TYPE_EXCHANGE_TOKEN)
            .requested_subject(keycloakUsername)
            .build());
    }

    public AccessTokenResponse login(String keycloakUsername, String password) {
        return this.keycloakClient.token(KeycloakAuthorizationRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .grant_type(GRANT_TYPE_PASSWORD)
            .username(keycloakUsername)
            .password(password)
            .build());
    }


    public void createUserAccount(String username, String password, UserTypeEnum userType) {
//        keycloakClient.adminToken(KeyclaokAuthorizationAdminRequest.builder()
//            .username("admin")
//            .password("admin_password")
//            .client_id("admin-cli")
//            .grant_type(GRANT_TYPE_PASSWORD)
//            .build());
        final UserRepresentation user = new UserRepresentation();

        user.setUsername(buildKeycloakUsername(username, userType));
        user.setEnabled(true);

        if (password != null && !password.isEmpty()) {
            final CredentialRepresentation credential = createCredential(password);
            user.setCredentials(List.of(credential));
        }

        final Response response = keycloak.getKeycloakInstance().realm(realm)
            .users()
            .create(user);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            log.info("Keycloak user created successfully!");
        } else {
            log.warn("Error creating keycloak user!");
            throw new KeycloakException("Keycloak User Error", response.getStatus());
        }
    }

    public CredentialRepresentation createCredential(String password) {
        final CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        return credential;
    }

    public UserRepresentation getUserRepresentation(String username, UserTypeEnum userType) {
        return keycloak.getKeycloakInstance().realm(realm)
            .users()
            .search(buildKeycloakUsername(username, userType))
            .stream().findFirst().get();
    }

    public void assignRolesToUser(String keycloakAccountId, Set<UserAccountRoleEnum> userAccountRoleSet) {
        // Get keycloak client
        ClientResource clientResource = getClientResourceByClientId(clientId);

        // Get keycloak user
        UserResource userResource = getUserResource(keycloakAccountId);
//
//        // Get keycloak roles IDs
        List<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        for (UserAccountRoleEnum userAccountRole : userAccountRoleSet) {
            RoleRepresentation roleRepresentation = findRoleByName(clientResource, userAccountRole);
            roleRepresentationList.add(roleRepresentation);
        }

        // Assign roles to user
        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(roleRepresentationList);
    }

    private RoleRepresentation findRoleByName(ClientResource clientResource, UserAccountRoleEnum userAccountRole) {
        return clientResource.roles().list().stream()
            .filter(role -> role.getName().equals(userAccountRole.toString()))
            .findFirst()
            // Should not happen, because roles are predefined in keycloak
            // if it happens, it's a configuration error
            .orElseThrow(() -> new KeycloakException(
                "Keycloak User Error" + userAccountRole,
                HttpStatus.NOT_FOUND.value())
            );
    }

    private UserResource getUserResource(String keycloakAccountId) {
        return keycloak.getKeycloakInstance().realm(realm)
            .users()
            .get(keycloakAccountId);
    }

    public void setPolice() {

    }

    public ClientResource getClientResourceByClientId(String clientId) {
        ClientRepresentation clientRepresentation = keycloak.getKeycloakInstance().realm(realm).clients().findByClientId(clientId).stream().findFirst().get();

        if (clientRepresentation == null) {
            // Should not happen, because client is predefined in keycloak
            // if it happens, it's a configuration error
            throw new KeycloakException("Client Not Found" + clientId, HttpStatus.NOT_FOUND.value());
        }

        return keycloak.getKeycloakInstance().realm(realm).clients().get(clientRepresentation.getId());
    }

    public void assignAttributesToUser(String keycloakAccountId, UserAttribute userAttribute) {
        // Get keycloak user
        UserResource userResource = getUserResource(keycloakAccountId);


    }
}
