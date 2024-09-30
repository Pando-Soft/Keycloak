package com.DemoKeyCloak.KeyCloak.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequest {

    private String username;
    private String password;
}
