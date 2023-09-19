package skhu.msg.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import skhu.msg.global.jwt.JwtFilter
import skhu.msg.global.jwt.TokenProvider

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? =
        http
            .httpBasic { httpBasic -> httpBasic.disable() }
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { sessionManagement -> sessionManagement.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .logout { logout -> logout.disable() }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/api-docs/**", "/api/v1/auth/**").permitAll()
                    .anyRequest().permitAll()
            }
            .cors { cors -> cors.configurationSource(configurationSource()) }
            .addFilterBefore(JwtFilter(tokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun configurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Access-Control-Allow-Credentials", "Authorization", "Set-Cookie")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }

}