package io.pelle.todo

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocumentationConfiguration {

    companion object {
        const val API_KEY = "api_key"
    }

    @Bean
    fun springShopOpenAPI(): OpenAPI? {
        return OpenAPI()
            .info(
                Info().title("Todo API")
                    .description("Sample application")
                    .version("v0.0.2")
                    .license(License().name("Apache 2.0"))
            )
            .components(
                Components()
                    .addSecuritySchemes(API_KEY, apiKeySecuritySchema())
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Github")
                    .url("https://github.com/pellepelster/kitchen-sink")
            )
    }

    fun apiKeySecuritySchema(): SecurityScheme? {
        return SecurityScheme()
            .name(API_KEY)
            .description("Bearer token returned by login")
            .`in`(SecurityScheme.In.HEADER)
            .type(SecurityScheme.Type.APIKEY)
    }
}
