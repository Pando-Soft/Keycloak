package com.DemoKeyCloak.KeyCloak.service.keycloak;

import com.DemoKeyCloak.KeyCloak.client.KeycloakClient;
import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.KeycloakAuthorizationRequest;
import com.DemoKeyCloak.KeyCloak.model.enums.UserAccountRoleEnum;
import com.DemoKeyCloak.KeyCloak.model.enums.UserTypeEnum;
import com.DemoKeyCloak.KeyCloak.model.exception.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.DemoKeyCloak.KeyCloak.config.KeyclaokSecurityUtil;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String GRANT_TYPE_EXCHANGE_TOKEN = "urn:ietf:params:oauth:grant-type:token-exchange";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String USER_IMPERSONATED_NAME = "user-impersonated.permission.users";

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final KeycloakClient keycloakClient;
    private final KeyclaokSecurityUtil keycloak;

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
        final UserRepresentation user = new UserRepresentation();

        user.setUsername(buildKeycloakUsername(username, userType));
        user.setEnabled(true);
        user.setEmailVerified(true);

        if (password != null && !password.isEmpty()) {
            final CredentialRepresentation credential = createCredential(password);
            user.setCredentials(List.of(credential));
        }

        final Response response = keycloak.getKeycloakInstance().realm(realm)
            .users()
            .create(user);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            log.info("Keycloak user created successfully!");
            createUserPolicy(user.getUsername());
            log.info("Create User Policy");
            assignUserImpersonatedPermission(realm, user.getUsername());
            log.info("Assign User Policy");
        } else {
            log.warn("Error creating keycloak user!");
            throw new KeycloakException(KeyclaokCode.KEYCLAOK_USER_ERROR.getValue());
        }
    }

    public void createAdminUserAccount(String username, String password, String email) {
        final UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            final CredentialRepresentation credential = createCredential(password);
            user.setCredentials(List.of(credential));
        }

        final Response response = keycloak.getKeycloakInstance().realm(realm)
            .users()
            .create(user);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            log.info("Keycloak admin created successfully!");
        } else {
            log.warn("Error creating keycloak admin!");
            throw new KeycloakException(KeyclaokCode.KEYCLAOK_USER_ERROR.getValue());
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

    public UserRepresentation getUserAdminRepresentation(String username) {
        return keycloak.getKeycloakInstance().realm(realm)
            .users()
            .search(username)
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
                KeyclaokCode.KEYCLAOK_USER_ERROR.getValue() + userAccountRole)
            );
    }

    private UserResource getUserResource(String keycloakAccountId) {
        return keycloak.getKeycloakInstance().realm(realm)
            .users()
            .get(keycloakAccountId);
    }

    public ClientResource getClientResourceByClientId(String clientId) {
        ClientRepresentation clientRepresentation = keycloak.getKeycloakInstance().realm(realm).clients().findByClientId(clientId).stream().findFirst().get();

        if (clientRepresentation == null) {
            // Should not happen, because client is predefined in keycloak
            // if it happens, it's a configuration error
            throw new KeycloakException(KeyclaokCode.CLIENT_NOT_FOUND.getValue() + clientId);
        }

        return keycloak.getKeycloakInstance().realm(realm).clients().get(clientRepresentation.getId());
    }

    public void createUserPolicy(String keycloakUsername) {
        UserRepresentation user = keycloak.getKeycloakInstance().realm(realm)
            .users()
            .search(keycloakUsername)
            .get(0);

        PolicyRepresentation policy = new PolicyRepresentation();
        policy.setId(keycloakUsername);
        policy.setName(keycloakUsername);
        policy.setType("user");
        policy.setLogic(Logic.POSITIVE);
        policy.setDecisionStrategy(DecisionStrategy.UNANIMOUS);
        policy.setDescription("");

        Map<String, String> config = new HashMap<>();
        String strFormat = "[\"" + user.getId() + "\"]";
        config.put("users", strFormat); // This is the user you are allowing to be impersonated
        policy.setConfig(config);

        ClientResource clientResource = getClientResource(realm, "realm-management");
        clientResource.authorization().policies().create(policy);
    }

    public void assignUserImpersonatedPermission(String realm, String policyName) {
        // Get the realm-management client
        ClientResource clientResource = getClientResource(realm, "realm-management");

        // Find the user-impersonated scope
        ScopeRepresentation userImpersonatedScope = clientResource.authorization().scopes()
            .findByName("user-impersonated");

        ScopePermissionRepresentation permissionRepresentation = clientResource.authorization().permissions().scope().findByName(USER_IMPERSONATED_NAME);
        permissionRepresentation.addPolicy(policyName);
//
//        if (userImpersonatedScope == null) {
//            throw new RuntimeException("User-impersonated scope not found");
//        }
//
//        // Find or create a resource permission for the user-impersonated scope
//        ResourcePermissionRepresentation userImpersonatedPermission = clientResource.authorization().permissions().resource()
//            .findByName(USER_IMPERSONATED_NAME);

//        if (userImpersonatedPermission == null) {
//            userImpersonatedPermission = new ResourcePermissionRepresentation();
//            userImpersonatedPermission.setName("user-impersonated");
//            userImpersonatedPermission.setType("scope");
//            userImpersonatedPermission.setScopes(Collections.singletonList(userImpersonatedScope));
//        }

        // Add the policy to the user-impersonated permission
//        Set<String> policies = new HashSet<>(new ArrayList<>(userImpersonatedPermission.getPolicies()));
//        policies.add(policyName); // Add the created policy to the permission
//        userImpersonatedPermission.setPolicies(policies);

        // Create or update the permission
//        if (userImpersonatedPermission.getId() == null) {
//            clientResource.authorization().permissions().resource().create(userImpersonatedPermission);
//        } else {
//            clientResource.authorization().permissions().resource().update(userImpersonatedPermission);
//        }
    }

    private ClientResource getClientResource(String realm, String clientId) {
        // Get the Keycloak client representation for the client by clientId
        ClientRepresentation clientRepresentation = keycloak.getKeycloakInstance().realm(realm)
            .clients()
            .findByClientId(clientId)
            .get(0);  // Get the first matching client (should be unique)

        // Get the ClientResource using the clientId
        return keycloak.getKeycloakInstance().realm(realm)
            .clients()
            .get(clientRepresentation.getId());  // This retrieves the ClientResource object
    }

}
