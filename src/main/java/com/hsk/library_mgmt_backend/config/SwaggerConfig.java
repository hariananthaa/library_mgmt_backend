package com.hsk.library_mgmt_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Library Service API", description = "Library API documentation", version = "1.0"), security = @SecurityRequirement(name = "oauth2_bearer"),
        servers = {@Server(url = "${server.servlet.context-path}", description = "Default Server URL")})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "JWT auth description", in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
}