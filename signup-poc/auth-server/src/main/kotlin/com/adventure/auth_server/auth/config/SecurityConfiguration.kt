package com.adventure.auth_server.auth.config

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

@Configuration
class SecurityConfiguration(private val roleBasedAuthenticationHandler: RoleBasedAuthenticationHandler) {

    @Bean
    @Order(value = 1)
    fun asFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
            .clientAuthentication { it.authenticationSuccessHandler(roleBasedAuthenticationHandler) }

        return httpSecurity
            .exceptionHandling {
                it.authenticationEntryPoint(
                    LoginUrlAuthenticationEntryPoint("/login")
                )
            }
            .build()
    }

    @Bean
    @Order(value = 2)
    fun defaultSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

        return httpSecurity
            .csrf {
                it
                    .ignoringRequestMatchers("/onboarding/submitCredentials")
            }
            .formLogin(Customizer.withDefaults())
            .authorizeHttpRequests {
                it.requestMatchers("/onboarding/**").permitAll()
                    .anyRequest().authenticated()
            }
            .build()
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings =
        AuthorizationServerSettings.builder()
            .build()

    @Bean
    fun servletWebServerFactory(): ServletWebServerFactory = TomcatServletWebServerFactory()
}