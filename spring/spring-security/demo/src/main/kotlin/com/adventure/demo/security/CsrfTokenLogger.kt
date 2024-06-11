package com.adventure.demo.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Component

@Component
class CsrfTokenLogger: Filter {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val csrfToken = servletRequest.getAttribute("_csrf") as CsrfToken
        logger.info("CSRF Token Header :: ${csrfToken.headerName}")
        logger.info("CSRF Token Parameter :: ${csrfToken.parameterName}")
        logger.info("CSRF Token Token :: ${csrfToken.token}")
        filterChain.doFilter(servletRequest, servletResponse)
    }

}