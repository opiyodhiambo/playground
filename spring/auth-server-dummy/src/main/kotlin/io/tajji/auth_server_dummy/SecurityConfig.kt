package io.tajji.auth_server_dummy

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.UUID

@Configuration
class SecurityConfig {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    @Order(1)
    fun asFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        logger.info("Applying default security filter chain")
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)
        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())

        httpSecurity.exceptionHandling {
            it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
        }
        return httpSecurity.build()
    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        logger.info("Applying default security filter chain for form login")
        return httpSecurity
            .formLogin(Customizer.withDefaults())
            .authorizeHttpRequests {
                it.requestMatchers("/auth").permitAll()
                    .anyRequest().authenticated()
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val encodedPassword = passwordEncoder.encode("RiekoIoane@13")
        val userDetails = User.withUsername("Mugo")
            .password(encodedPassword)
            .roles("LANDLORD")
            .build()

        return InMemoryUserDetailsManager(userDetails)
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("tajjiboma")
            .clientSecret(passwordEncoder().encode("tuzid"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope(OidcScopes.OPENID)
            .redirectUri("https://tajji.io/")
            .build()

        return InMemoryRegisteredClientRepository(registeredClient)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)

        val keyPair = keyPairGenerator.generateKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        val rsaKey = RSAKey.Builder(publicKey)
            .keyID(UUID.randomUUID().toString())
            .privateKey(privateKey)
            .build()

        val jwkSet = JWKSet(rsaKey)

        return ImmutableJWKSet(jwkSet)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings
            .builder()
            .build()
    }

    @Bean
    fun customizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer {
            val claims = it.claims
            val portfolioClaims = listOf(
                PortfolioClaims(portfolioId = UUID.randomUUID(), portfolioName = "Millways"),
                PortfolioClaims(portfolioId = UUID.randomUUID(), portfolioName = "Elevate"),
                PortfolioClaims(portfolioId = UUID.randomUUID(), portfolioName = "Joyland")
            )
            claims.claim("Porfolios", portfolioClaims)

        }
    }
}

data class PortfolioClaims(
    val portfolioId: UUID,
    val portfolioName: String
)