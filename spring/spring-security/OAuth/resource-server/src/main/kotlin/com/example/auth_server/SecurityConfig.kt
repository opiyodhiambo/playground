package com.example.auth_server

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Value("\${keySetURI}")
    private lateinit var keySetUri: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.build()
    }

}