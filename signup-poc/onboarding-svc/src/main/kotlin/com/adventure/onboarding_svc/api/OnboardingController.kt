package com.adventure.onboarding_svc.api

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OnboardingController {

    @GetMapping(GET_ACCOUNT_STATUS)
    fun getAccountStatus(): ResponseEntity<GetAccountStatusResponse> {
        val randomStatus = AccountStatus.entries.random()
        return ResponseEntity.ok(GetAccountStatusResponse(accountStatus = randomStatus))
    }

    companion object {
        const val GET_ACCOUNT_STATUS = "/onboarding/getAccountStatus"
    }
}

enum class AccountStatus(status: String) {
    ACTIVE("ACTIVE"),
    EMAIL_VERIFICATION_PENDING("EMAIL VERIFICATION PENDING"),
    PHONE_NUMBER_VERIFICATION_PENDING("PHONE NUMBER VERIFICATION PENDING"),
    KYC_INITIATED("KYC INITIATED"),
    KYC_FAILED("KYC FAILED"),
    KYC_PASSED("KYC PASSED"),
}

data class GetAccountStatusResponse(
    @JsonProperty("account_status") val accountStatus: AccountStatus
)