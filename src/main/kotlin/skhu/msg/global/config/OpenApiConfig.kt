package skhu.msg.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        val info = Info()
            .title("MSG API")
            .description("MSG API 명세서")
            .version("0.0.1")

        val authName = "JWT 토큰"

        val securityRequirement = SecurityRequirement().addList(authName)

        val components = Components()
            .addSecuritySchemes(
                authName,
                SecurityScheme()
                    .name(authName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat("JWT")
                    .description("accessToken 입력하세요. (Bearer 필요없음)"))

        return OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components)
    }

}