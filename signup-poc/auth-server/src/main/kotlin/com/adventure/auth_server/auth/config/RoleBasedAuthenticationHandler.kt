package com.adventure.auth_server.auth.config

import com.adventure.auth_server.auth.components.CustomUserDetails
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class RoleBasedAuthenticationHandler() : AuthenticationSuccessHandler {

    private val successRedirectUrl = "https://1d1dc82944ab93060cbb7abd4449a0b0.serveo.net/loginSuccess"
    private val failureRedirectUrl = "https://1d1dc82944ab93060cbb7abd4449a0b0.serveo.net/platformOnboarding"

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val userPrincipal = authentication.details as? CustomUserDetails
            ?: throw IllegalStateException("Unexpected principal type")
        val redirectUrl = if (userPrincipal.getAccountStatus() == AccountStatus.ACTIVE) {
            successRedirectUrl
        } else {
            failureRedirectUrl
        }
        response.sendRedirect(redirectUrl)
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

