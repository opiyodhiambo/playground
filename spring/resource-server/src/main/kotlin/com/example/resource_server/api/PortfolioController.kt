package com.example.resource_server.api

import com.example.resource_server.security.CustomAuthentication
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class PortfolioController {

    @GetMapping("/portfolio")
    @PreAuthorize("hasRole('LANDLORD')")
    fun getLandlordPortfolios(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is CustomAuthentication) {
            val portfolio = authentication.getPortfolios()
            val propertyNames = portfolio.joinToString(separator = ", ") { it.propertyName }
            return "You have ${portfolio.size} properties in your portfolio :: $propertyNames"
        }
        return "Authentication is not of type CustomAuthentication"
    }

    @GetMapping("/property")
    @PreAuthorize("hasRole('TENANT')")
    fun getTenantProperty(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is CustomAuthentication) {
            val portfolio = authentication.getPortfolios()
            val propertyName = portfolio[Random.nextInt(portfolio.size)].propertyName
            return "You are a tenant of $propertyName"
        }
        return "Authentication is not of type CustomAuthentication"
    }

}