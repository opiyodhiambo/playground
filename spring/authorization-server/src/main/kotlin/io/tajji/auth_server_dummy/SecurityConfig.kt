package io.tajji.auth_server_dummy


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE
import org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
import org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

import java.util.UUID

@Configuration
class AppConfig {

    @Bean
    @Order(1)
    fun asFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(http)


        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())

        http.exceptionHandling {
            it.authenticationEntryPoint(
                LoginUrlAuthenticationEntryPoint("/login")
            )
        }

        return http.build()

    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.formLogin(Customizer.withDefaults())

        http.authorizeHttpRequests {
            it.requestMatchers("/error").permitAll()
            it.anyRequest().authenticated()
        }

        return http.build()

    }

    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> =
        OAuth2TokenCustomizer {
            it.claims.claim("priority", "HIGH")
        }

}

@Configuration
class ClientManagementConf {

    @Bean fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientName("Custom")
            .clientSecret("secret")
            .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
            .authorizationGrantType(AUTHORIZATION_CODE)
            .authorizationGrantType(REFRESH_TOKEN)
            .redirectUri("https://tajji.io")
            .scope(OPENID)
            .build()

        return InMemoryRegisteredClientRepository(listOf(registeredClient))
    }
}






