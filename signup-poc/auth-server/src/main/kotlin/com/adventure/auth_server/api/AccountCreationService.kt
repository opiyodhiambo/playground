package com.adventure.auth_server.api

import com.adventure.auth_server.auth.components.InMemoryUserStore
import com.adventure.auth_server.auth.components.JwtTokenService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AccountCreationService(
    private val inMemoryUserStore: InMemoryUserStore,
    private val jwtTokenService: JwtTokenService
) {
    fun createAndAuthenticateAccount(request: SubmitCredentialsRequest): ResponseEntity<Any> {
        if (!request.termsOfUse) {
            throw RuntimeException("Please read and consent to the terms of service")
        } else {
            createAccount(emailAddress = request.emailAddress, password = request.password)
            val token = authenticateAccount(emailAddress = request.emailAddress, password = request.password)
            val headers = HttpHeaders().apply {
                set(HttpHeaders.AUTHORIZATION, "Bearer $token")
                set(HttpHeaders.LOCATION, "https://tajji.io")
            }

            return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build()
        }
    }

    private fun authenticateAccount(emailAddress: String, password: String): String {
        return if (inMemoryUserStore.authenticate(emailAddress = emailAddress, password = password)) {
            jwtTokenService.generateToken(username = emailAddress)
        } else {
            throw IllegalArgumentException("Authentication failed")
        }
    }

    private fun createAccount(emailAddress: String, password: String) {
        inMemoryUserStore.addUser(emailAddress = emailAddress, password = password)
    }
}