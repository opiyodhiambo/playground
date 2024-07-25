package com.adventure.auth_server.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountCreationController(private val accountCreationService: AccountCreationService) {

    @PostMapping(SUBMIT_CREDENTIALS)
    fun submitCredentials(@RequestBody submitCredentialsRequest: SubmitCredentialsRequest): ResponseEntity<Void> {
        return accountCreationService.createAndAuthenticateAccount(request = submitCredentialsRequest)
    }

    companion object {
        const val SUBMIT_CREDENTIALS = "/onboarding/submitCredentials"

    }
}

data class SubmitCredentialsRequest(
    val emailAddress: String,
    val password: String,
    val termsOfUse: Boolean
)