package com.adventure.catalog_service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
class DataConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> = AuditorAware {
        Optional.ofNullable(SecurityContextHolder.getContext().authentication)
            .filter { it.isAuthenticated }
            .map { it.name }
    }
}