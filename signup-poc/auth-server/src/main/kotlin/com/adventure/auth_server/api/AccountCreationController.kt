package com.adventure.auth_server.api

import com.adventure.auth_server.auth.components.CustomAuthenticationProvider
import com.adventure.auth_server.auth.config.AccountStatus
import com.adventure.auth_server.auth.persistence.AccountRole
import com.adventure.auth_server.auth.persistence.UsersEntity
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*

@RestController
class AccountCreationController(
    private val accountCreationService: AccountCreationService,
    private val customAuthenticationProvider: CustomAuthenticationProvider
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(SUBMIT_CREDENTIALS)
    fun submitCredentials(@RequestBody submitCredentialsRequest: SubmitCredentialsRequest): ResponseEntity<Void> {
        return accountCreationService.createAndAuthenticateAccount(request = submitCredentialsRequest)
    }

//    @PostMapping("/login")
//    fun defaultLoginEndpoint(
//        @RequestParam formData: Map<String, String>,
//        httpServletRequest: HttpServletRequest
//    ): Authentication {
//
//        val username = formData["username"]
//        val password = formData["password"]
//
//        val csrfToken = httpServletRequest.getHeader("X-CSRF-TOKEN")
//        logger.info("csrfToken used :: $csrfToken")
//        val authenticationRequest =
//            UsernamePasswordAuthenticationToken
//                .unauthenticated(username, password)
//
//        return customAuthenticationProvider.authenticate(authenticationRequest)
//    }

    @PostMapping("/authenticateUser")
    fun authenticateUser(@RequestBody loginRequest: LoginRequest): Authentication {
        val authenticationRequest =
        UsernamePasswordAuthenticationToken
            .unauthenticated(loginRequest.username, loginRequest.password)
        return customAuthenticationProvider.authenticate(authenticationRequest)

    }

    @GetMapping("/customLoginPage")
    fun customLoginPage(httpServletRequest: HttpServletRequest): ResponseEntity<Void> {
        val headers = HttpHeaders()
        logger.info("Request Headers :: ${httpServletRequest.getHeader("Origin")}")
        headers.location = URI.create("http://localhost:3000/login")
        logger.info("Headers are :: $headers")
        return ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    companion object {
        const val SUBMIT_CREDENTIALS = "/onboarding/submitCredentials"

    }
    data class LoginRequest(
        @JsonProperty("username") val username: String,
        @JsonProperty("password") val password: String,
    )
    data class SubmitCredentialsRequest(
        val emailAddress: String,
        val password: String,
        val termsOfUse: Boolean
    )
}
