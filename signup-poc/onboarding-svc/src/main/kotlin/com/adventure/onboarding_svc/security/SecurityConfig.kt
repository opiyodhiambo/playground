package com.adventure.onboarding_svc.security

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.crypto.SecretKey

@Configuration
class SecurityConfig {

    @Value("\${jwt.secret-key}")
    private lateinit var secretKeyString: String

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

        return httpSecurity
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests { it.anyRequest().authenticated() }

            .oauth2ResourceServer {
                it
                    .jwt { jwt ->
                    jwt.decoder(jwtDecoder()) // Use the custom jwtDecoder
                }
            }
            .build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        // Create a JwtDecoder that will validate tokens using the secret key
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf("*")
        corsConfiguration.allowedMethods = listOf("GET", "POST")
        corsConfiguration.allowedHeaders = listOf("*")
        corsConfiguration.exposedHeaders = listOf("Authorization")
        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return urlBasedCorsConfigurationSource
    }


}