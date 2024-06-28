package com.example.resource_server.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

class CustomAuthentication(
    jwt: Jwt,
    authorities: Collection<GrantedAuthority>,
    private val portfolio: List<PropertyClaims>
) : JwtAuthenticationToken(jwt, authorities) {

    fun getPortfolios(): List<PropertyClaims> {
        return portfolio
    }

}

data class PropertyClaims(
    val propertyId: UUID,
    val propertyName: String
)

