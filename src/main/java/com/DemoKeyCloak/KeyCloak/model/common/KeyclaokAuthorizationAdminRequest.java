package com.DemoKeyCloak.KeyCloak.model.common;

import lombok.Builder;

@Builder
public class KeyclaokAuthorizationAdminRequest {

    String username;
    String password;
    String client_id;
    String grant_type;
}
