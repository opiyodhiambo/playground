package com.adventure.auth_server.auth.components

import com.adventure.auth_server.auth.persistence.UsersRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val usersRepository: UsersRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUserName(username = username)
            .orElseThrow { NoSuchElementException("username $username not found") }
        return CustomUserDetails(usersEntity = user)
    }

    fun authenticateNewAccount(emailAddress: String, password: String): Boolean {
        val userDetails = loadUserByUsername(emailAddress)
        return userDetails.password == password
    }
}

@Component
class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val userName = authentication.name
        val password = authentication.credentials.toString()
        val userDetails = userDetailsService.loadUserByUsername(userName)

        if (passwordEncoder.matches(password, userDetails.password)) {
            return UsernamePasswordAuthenticationToken(userName, password)
        } else throw BadCredentialsException("Invalid Credentials")
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}