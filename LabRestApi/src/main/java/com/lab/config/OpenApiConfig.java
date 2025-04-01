package com.lab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API для Лабораторной информационной системы",
                version = "2.1"
        )
)

@SuppressWarnings("unused")
public class OpenApiConfig {

    @Bean
    @SuppressWarnings("unused")
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .packagesToScan("com.lab.controller")
                .build();
    }
}
