package io.tajji.auth_server_dummy

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType.*
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
import org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.util.*

@Configuration
class UserManagementConf {

    @Bean
    fun userDetailsService(): UserDetailsService {

        val bill = User.builder()
            .username("bill")
            .password("bill123")
            .roles("writer")
            .build()

        return InMemoryUserDetailsManager(listOf(bill))

    }

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
//            .postLogoutRedirectUri("https://799c-41-90-124-47.ngrok-free.app/logoutSuccess")
            .build()

        return InMemoryRegisteredClientRepository(listOf(registeredClient))
    }

    @Bean fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

}