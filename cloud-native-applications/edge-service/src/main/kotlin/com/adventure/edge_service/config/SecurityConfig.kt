package com.adventure.edge_service.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository

@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun authorizedClientRepository(): ServerOAuth2AuthorizedClientRepository {
        return WebSessionServerOAuth2AuthorizedClientRepository()
    }
}