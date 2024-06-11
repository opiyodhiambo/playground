package com.adventure.demo.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.DefaultCsrfToken
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomCsrfTokenRepository(private val csrfTokenRepository: TokenRepository) : CsrfTokenRepository {
    override fun generateToken(request: HttpServletRequest): CsrfToken {
        val randomId = UUID.randomUUID().toString()
        return DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", randomId)
    }

    override fun saveToken(
        token: CsrfToken,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val tokenIdentifier = request.getHeader("X-IDENTIFIER")
        val existingToken = csrfTokenRepository.findTokenByIdentifier(identifier = tokenIdentifier)

        if (existingToken.isPresent) {
            val csrfToken = existingToken.get()
            csrfToken.token = token.token
        } else {
            val newToken = Token().apply {
                this.token = token.token
                this.identifier = tokenIdentifier
            }
            csrfTokenRepository.save(newToken)
        }
    }

    override fun loadToken(request: HttpServletRequest): CsrfToken? {
        val tokenIdentifier = request.getHeader("X-IDENTIFIER")
        val existingToken = csrfTokenRepository.findTokenByIdentifier(identifier = tokenIdentifier)
        return if (existingToken.isPresent) {
             DefaultCsrfToken(
                "X-CSRF-TOKEN",
                "_csrf",
                existingToken.get().token
            )
        }else {
            null
        }
    }
}