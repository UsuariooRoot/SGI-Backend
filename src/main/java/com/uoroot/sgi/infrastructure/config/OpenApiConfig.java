package com.uoroot.sgi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * This class defines the global configuration for API documentation,
 * including general information, servers, security and tags.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sgiOpenAPI() {

        // Contact information
        Contact contact = new Contact()
                .name("Soporte SGI")
                .email("dantevilchez860@gmail.com")
                .url("https://github.com/UsuariooRoot");

        // License information
        License license = new License()
                .name("Apache License, Version 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        // API general information
        Info info = new Info()
                .title("Sistema de Gestión de Incidentes API")
                .version("1.0.0")
                .contact(contact)
                .description("API RESTful para el Sistema de Gestión de Incidentes (SGI). " +
                        "Proporciona endpoints para la gestión completa de tickets, incidentes, " +
                        "empleados y autenticación de usuarios.")
                // .termsOfService("https://www.uoroot.com/terms")
                .license(license);

        // JWT security configuration
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("Ingrese el token JWT con el prefijo Bearer: 'Bearer {token}'");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        // Definition of tags to group endpoints
        List<Tag> tags = Arrays.asList(
                new Tag().name("Autenticación")
                        .description("Operaciones relacionadas con la autenticación de usuarios"),
                new Tag().name("Tickets").description("Gestión de tickets de incidentes"),
                new Tag().name("Incidentes").description("Gestión de incidentes y categorías"),
                new Tag().name("Empleados").description("Gestión de información de empleados"));

        return new OpenAPI()
                .info(info)
                // .servers(List.of(server))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .tags(tags);
    }
}