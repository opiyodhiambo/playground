package com.adventure.auth_server.auth.config

import com.adventure.auth_server.auth.components.CustomUserDetailsService
import com.adventure.auth_server.auth.components.InMemoryUserStore
import com.adventure.auth_server.auth.persistence.UsersRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE
import org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.util.*

@Configuration
class ClientConfiguration(private val usersRepository: UsersRepository) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

//    @Bean
//    fun userDetailsService(): UserDetailsService {
//        return CustomUserDetailsService(usersRepository = usersRepository)
//    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("rieko@example.com")
            .password(passwordEncoder().encode("12345678"))
            .roles("read")
            .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {

        val client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientName("Custom")
            .clientSecret("secret")
            .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
            .authorizationGrantType(AUTHORIZATION_CODE)
            .authorizationGrantType(REFRESH_TOKEN)
            .postLogoutRedirectUri("https://tajji.io/logoutSuccess")
            .redirectUri("https://tajji.io")
            .scope(OidcScopes.OPENID)
            .build()

        return InMemoryRegisteredClientRepository(listOf(client))

    }
}