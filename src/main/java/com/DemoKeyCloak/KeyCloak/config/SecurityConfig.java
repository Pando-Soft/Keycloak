package com.DemoKeyCloak.KeyCloak.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authz) ->
            authz.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/user/**").hasRole(USER)
                .anyRequest().authenticated());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
}
