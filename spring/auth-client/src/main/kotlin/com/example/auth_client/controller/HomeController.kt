package com.example.auth_client.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import kotlin.io.encoding.Base64

@Controller
class HomeController(private val clientManager: OAuth2AuthorizedClientManager) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @GetMapping("/getToken")
    fun getToken(): String {
        val request = OAuth2AuthorizeRequest
            .withClientRegistrationId("custom-auth-server")
            .principal("client")
            .build()

        val client = clientManager.authorize(request)

        return if (client != null) {
            logger.info("The token value is ${client.accessToken.tokenValue}")
             client.accessToken.tokenValue
        } else "Client Unauthorized"
    }
}