package com.adventure.auth_server.api

import com.adventure.auth_server.auth.components.CustomAuthenticationProvider
import com.adventure.auth_server.auth.components.CustomUserDetailsService
import com.adventure.auth_server.auth.components.JwtTokenService
import com.adventure.auth_server.auth.config.AccountStatus
import com.adventure.auth_server.auth.persistence.AccountRole
import com.adventure.auth_server.auth.persistence.UsersEntity
import com.adventure.auth_server.auth.persistence.UsersRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountCreationService(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtTokenService: JwtTokenService,
    private val usersRepository: UsersRepository,
    private val customAuthenticationProvider: CustomAuthenticationProvider
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createAndAuthenticateAccount(request: SubmitCredentialsRequest): ResponseEntity<String> {
        if (!request.termsOfUse) {
            throw RuntimeException("Please read and consent to the terms of service")
        } else {
            createAccount(emailAddress = request.emailAddress, password = request.password)
            val token = authenticateAccount(emailAddress = request.emailAddress, password = request.password)
            logger.info("generated token :: $token")
            val headers = HttpHeaders().apply {
                set(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
            logger.info("generated headers :: $headers")
            return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body("Karibu Tajji")

        }
    }

    private fun authenticateAccount(emailAddress: String, password: String): String {
        return if (customUserDetailsService.authenticateNewAccount(emailAddress = emailAddress, password = password)) {
            jwtTokenService.generateToken(username = emailAddress)
        } else {
            throw IllegalArgumentException("Authentication failed")
        }
    }

    private fun createAccount(emailAddress: String, password: String) {
        val usersEntity = UsersEntity.newUser(
            principalId = UUID.randomUUID(),
            userName = emailAddress,
            password = password,
            status = AccountStatus.EMAIL_VERIFICATION_PENDING,
            role = AccountRole.LANDLORD
        )

        usersRepository.save(usersEntity)
    }
}