package com.DemoKeyCloak.KeyCloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KeyCloakApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyCloakApplication.class, args);
    }

}
