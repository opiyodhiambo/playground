package com.adventure.auth_server.api

import com.adventure.auth_server.api.AccountCreationController.Companion.ONBOARDING
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ONBOARDING)
class AccountCreationController(private val accountCreationService: AccountCreationService) {

    @PostMapping(SUBMIT_CREDENTIALS)
    fun submitCredentials(@RequestBody submitCredentialsRequest: SubmitCredentialsRequest) {
        accountCreationService.createAndAuthenticateAccount(request = submitCredentialsRequest)
    }

    companion object {
        const val ONBOARDING = "/onboarding/"
        const val SUBMIT_CREDENTIALS = "/submitCredentials"

    }
}

data class SubmitCredentialsRequest(
    val emailAddress: String,
    val password: String,
    val termsOfUse: Boolean
)