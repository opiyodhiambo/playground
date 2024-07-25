package io.tajji.auth_server_dummy

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    private val redirectStrategy = DefaultRedirectStrategy()
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?     ) {
        if (authentication != null) {
            val principal = authentication.principal
            if (principal is User) {
                val roles = principal.authorities.map { SimpleGrantedAuthority(it.authority) }
                when {
                    roles.contains(SimpleGrantedAuthority("LANDLORD")) -> redirectStrategy.sendRedirect(
                        request,
                        response,
                        "http://tajji.io/landlords"
                    )

                    roles.contains(SimpleGrantedAuthority("RESIDENT")) -> redirectStrategy.sendRedirect(
                        request,
                        response,
                        "http://tajji.io/residents"
                    )

                    else -> redirectStrategy.sendRedirect(request, response, "default_redirect_url")
                }
            }
        }
    }
}


