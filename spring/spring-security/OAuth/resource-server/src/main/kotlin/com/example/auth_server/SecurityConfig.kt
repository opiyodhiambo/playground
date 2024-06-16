package com.example.auth_server

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManagerResolver
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val converter: JWTAuthenticationConverter) {

    @Value("\${keySetURI}")
    private lateinit var keySetUri: String
    @Value("\${resourceServer.clientId}")
    private lateinit var clientId: String
    @Value("\${resourceServer.clientSecret}")
    private lateinit var clientSecret: String
    @Value("\${introspectionURI}")
    private lateinit var introspectionUri: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .oauth2ResourceServer {
                // jwt and opaqueTokens are alternatives. Not intended to be used together
                it.jwt { jwt ->
                    jwt.jwkSetUri(keySetUri)
                        .jwtAuthenticationConverter(converter)
                }
                it.opaqueToken { token ->
                    token.introspectionUri(introspectionUri)
                        .introspectionClientCredentials(clientId, clientSecret)
                }
                // For multi-tenancy scenarios
                it.authenticationManagerResolver(authenticationManagerResolver(
                    jwtDecoder = jwtDecoder(),
                    opaqueTokenIntrospector = opaqueTokenIntrospector()
                ))
            }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .build()
    }

    // For multi-tenancy scenarios
    @Bean
    fun authenticationManagerResolver(jwtDecoder: JwtDecoder, opaqueTokenIntrospector: OpaqueTokenIntrospector ): AuthenticationManagerResolver<HttpServletRequest> {

//      Two different AuthServers, one issuing opaque tokens and another issuing JWT
        val jwtAuth = ProviderManager(JwtAuthenticationProvider(jwtDecoder))
        val opaqueAuth = ProviderManager(OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector))

        return AuthenticationManagerResolver {
            when (it.getHeader("type")) {
                "jwt" -> jwtAuth
                else -> opaqueAuth
            }
        }

//        two different AuthServers issuing the JWT tokens
//        return JwtIssuerAuthenticationManagerResolver(
//            "http://localhost:7070",
//            "http://localhost:8080"
//        )
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder
            .withJwkSetUri(keySetUri)
            .build()
    }

    @Bean
    fun opaqueTokenIntrospector(): OpaqueTokenIntrospector {
        return SpringOpaqueTokenIntrospector(
            introspectionUri, clientId, clientSecret
        )
    }

}