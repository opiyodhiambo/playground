package com.adventure.order_service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun filterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return serverHttpSecurity
            .authorizeExchange {
                it.anyExchange().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt {  }
            }
            .requestCache {
                it.requestCache(NoOpServerRequestCache.getInstance())
            }
            .csrf {
                it.disable()
            }
            .build()
    }
}