package com.adventure.auth_server.auth.config

import com.adventure.auth_server.auth.components.CustomAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration(
    private val roleBasedAuthenticationHandler: RoleBasedAuthenticationHandler,
    private val customAuthenticationProvider: CustomAuthenticationProvider
) {

    private val loggerFactory = LoggerFactory.getLogger(this::class.java)
    @Bean
    @Order(value = 1)
    fun asFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
            .clientAuthentication {
                it.authenticationSuccessHandler(roleBasedAuthenticationHandler)
            }

        return httpSecurity
            .exceptionHandling {
                it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
            }
            .build()
    }

    @Bean
    @Order(value = 2)
    fun defaultSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

        return httpSecurity
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .formLogin {
                it.loginPage("/login").permitAll()
                    .successHandler { _, response, _ ->
                        response.status = HttpStatus.FOUND.value()
                        response.setHeader("Location", "https://tajji.io")
                    }
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/onboarding/**").permitAll()
                    .requestMatchers("/customLoginPage").permitAll()
                    .requestMatchers("/authenticateUser").permitAll()
                    .anyRequest().authenticated()
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf("https://tajji.io")
        corsConfiguration.allowedMethods = listOf("GET", "POST", "OPTIONS")
        corsConfiguration.allowedHeaders = listOf("Authorization", "Content-Type")
        corsConfiguration.exposedHeaders = listOf("Authorization", "Location")
        corsConfiguration.allowCredentials = true

        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return urlBasedCorsConfigurationSource
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(customAuthenticationProvider)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings =
        AuthorizationServerSettings.builder()
            .build()

    @Bean
    fun servletWebServerFactory(): ServletWebServerFactory = TomcatServletWebServerFactory()
}