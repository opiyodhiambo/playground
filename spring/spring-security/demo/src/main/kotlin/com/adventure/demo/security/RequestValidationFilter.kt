package com.adventure.demo.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class RequestValidationFilter: Filter {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val httpRequest = servletRequest as HttpServletRequest
        val httpResponse = servletResponse as HttpServletResponse
        val requestId = httpRequest.getHeader("request_id")

        if (requestId.isNullOrEmpty())  {
            httpResponse.status = HttpServletResponse.SC_BAD_REQUEST
            return
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

}