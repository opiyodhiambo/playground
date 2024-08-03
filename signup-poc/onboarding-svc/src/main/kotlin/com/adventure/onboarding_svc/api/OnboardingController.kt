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

enum class AccountStatus(val value: String) {
    EMAIL_ADDRESS_VERIFICATION_PENDING("EMAIL ADDRESS VERIFICATION PENDING"),
//    PHONE_NUMBER_VERIFICATION_PENDING("PHONE NUMBER VERIFICATION PENDING"),
//    KYC_PENDING("KYC PENDING"),
//    KYC_INITIATED("KYC INITIATED"),
//    PAYMENT_METHOD_LINKING_PENDING("PAYMENT METHOD LINKING PENDING"),
    ACTIVE("ACTIVE");
//    SUSPENDED("SUSPENDED");

    fun canTransitionTo(status: AccountStatus): Boolean = when (this) {
        EMAIL_ADDRESS_VERIFICATION_PENDING -> setOf(ACTIVE).contains(status)
//        PHONE_NUMBER_VERIFICATION_PENDING -> setOf(KYC_PENDING).contains(status)
//        KYC_PENDING -> setOf(KYC_INITIATED).contains(status)
//        KYC_INITIATED -> setOf(KYC_PENDING, PAYMENT_METHOD_LINKING_PENDING, ACTIVE).contains(status)
//        PAYMENT_METHOD_LINKING_PENDING -> setOf(ACTIVE).contains(status)
        ACTIVE -> setOf(ACTIVE).contains(status)
//        SUSPENDED -> setOf(ACTIVE).contains(status)
    }
}

data class GetAccountStatusResponse(
    @JsonProperty("account_status") val accountStatus: AccountStatus
)