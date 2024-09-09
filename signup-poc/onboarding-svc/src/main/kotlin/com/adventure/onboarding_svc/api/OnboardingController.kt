package com.adventure.onboarding_svc.api

import com.adventure.onboarding_svc.api.OnboardingController.Companion.REQUEST_NEW_EMAIL_VERIFICATION_CODE
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OnboardingController {

    @GetMapping(GET_ACCOUNT_STATUS)
    fun getAccountStatus(): ResponseEntity<GetAccountStatusResponse> {
        val randomStatus = AccountStatus.entries.random()
        return ResponseEntity.ok(GetAccountStatusResponse(accountStatus = randomStatus))
    }

    @PostMapping(SUBMIT_EMAIL_VERIFICATION_CODE)
    fun submitEmailVerificationCode(@RequestBody submitEmailVerificationCodeRequest: SubmitEmailVerificationCodeRequest): ResponseEntity<Any> {
        return if (submitEmailVerificationCodeRequest.verificationCode == VERIFICATION_CODE) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().body("Invalid Verification code")
        }
    }

    @PostMapping(REQUEST_NEW_EMAIL_VERIFICATION_CODE)
    fun requestNewEmailVerificationCode(): ResponseEntity<Unit> = ResponseEntity.accepted().build()

    companion object {
        const val GET_ACCOUNT_STATUS = "/onboarding/getAccountStatus"
        const val SUBMIT_EMAIL_VERIFICATION_CODE = "/onboarding/submitEmailVerificationCode"
        const val REQUEST_NEW_EMAIL_VERIFICATION_CODE = "/onboarding/requestNewEmailVerificationCode"
        private const val VERIFICATION_CODE = "123456"
    }
}

enum class AccountStatus(val value: String) {
    EMAIL_ADDRESS_VERIFICATION_PENDING("EMAIL ADDRESS VERIFICATION PENDING"),
    ACTIVE("ACTIVE");

    fun canTransitionTo(status: AccountStatus): Boolean = when (this) {
        EMAIL_ADDRESS_VERIFICATION_PENDING -> setOf(ACTIVE).contains(status)
        ACTIVE -> setOf(ACTIVE).contains(status)
    }
}

data class GetAccountStatusResponse(
    @JsonProperty("account_status") val accountStatus: AccountStatus
)
data class SubmitEmailVerificationCodeRequest(
    @JsonProperty("verification_code") val verificationCode: String
)