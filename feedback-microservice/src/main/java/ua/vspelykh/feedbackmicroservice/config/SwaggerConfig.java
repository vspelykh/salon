package ua.vspelykh.feedbackmicroservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "feedback"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("feedback")
                .packagesToScan("ua.vspelykh.feedbackmicroservice.controller")
                .packagesToExclude("ua.vspelykh.feedbackmicroservice.controller.**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("cloud API")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .tags(Collections.singletonList(new Tag().name("General Information")
                        .description("Please note that when interacting with the API outside of Swagger, replace the " +
                                "base path \"/api/v1\" with \"/feedback\". This adjustment ensures proper communication with " +
                                "the API endpoints in non-Swagger environments")));
    }
}

