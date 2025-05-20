package com.universidad.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Registro Universitario",
        version = "1.0.0",
        description = "CRUD para sistema universitario"
    )
)
public class OpenApiConfig {
    
}
