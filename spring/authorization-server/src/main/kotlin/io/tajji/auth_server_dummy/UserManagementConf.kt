package io.tajji.auth_server_dummy

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

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

    @Bean fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

}