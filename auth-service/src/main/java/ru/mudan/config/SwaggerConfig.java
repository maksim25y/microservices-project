package ru.mudan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Микросервис auth-service",
        description = "Микросервис для работы с безопасностью, взаимодействует с keycloak", version = "1.0.0",
        contact = @Contact(
            name = "GitHub",
            url = "https://github.com/maksim25y/microservices-project"
        )
    ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Auth-Service (порт 8082)"),
                @Server(url = "http://localhost:8765", description = "API Gateway (порт 8765)")
        }
)

@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig { }
