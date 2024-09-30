package com.DemoKeyCloak.KeyCloak.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {
    @Server(url = "${com.DemoKeyCloak.swagger.server-url}", description = "Default Server URL")
})
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearer-key";

        var components = new Components()
            .addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));

        var info = new Info()
            .title("Demo API")
            .description("This is a sample Spring Boot REST API using Swagger")
            .version("v1.0")
            .license(new License().name("Apache 2.0")
                .url("http://springdoc.org"));

        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(components)
            .info(info);
    }
}
