package com.adventure.order_service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableR2dbcAuditing
class DataConfig {

    @Bean
    fun auditorAware(): ReactiveAuditorAware<String> = ReactiveAuditorAware {
         ReactiveSecurityContextHolder.getContext()
            .map { it.authentication }
            .filter { it.isAuthenticated }
            .map { it.name }
    }
}