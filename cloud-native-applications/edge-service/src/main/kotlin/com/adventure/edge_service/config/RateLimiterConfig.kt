package com.adventure.edge_service.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class RateLimiterConfig {

    @Bean
    fun keyResolver() = KeyResolver { Mono.just("anonymous") }
}