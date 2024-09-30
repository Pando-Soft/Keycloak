package com.DemoKeyCloak.KeyCloak.model.common;

import lombok.Builder;

@Builder
public class KeycloakAuthorizationRequest {

    String client_id;
    String client_secret;
    String grant_type;

    /**
     * Used for password grant loginType in basic login flow.
     */
    String username;
    String password;

    /**
     * Used for token exchange grant loginType in passwordless OTP login flow.
     * <br>
     * When user provides a valid OTP, token exchange authorization request is sent to retrieve access token of the user.
     */
    String requested_subject;

    /**
     * Used for refresh token grant loginType in token refresh flow.
     */
    String refresh_token;
}
