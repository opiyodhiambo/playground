package com.example.resource_server.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtAuthenticationConverter : Converter<Jwt, CustomAuthentication> {

    @Suppress("UNCHECKED_CAST")
    override fun convert(source: Jwt): CustomAuthentication {
        val authorities = source
            .getClaimAsStringList("authorities")?.map {
                GrantedAuthority { it }
            } ?: emptyList()
        val portfolio = (source.claims["Porfolio"] as? List<Map<String, String>>)?.map {
            PropertyClaims(
                propertyId = UUID.fromString(it["propertyId"] as String),
                propertyName = it["propertyName"] as String
            )
        }
        return CustomAuthentication(
            jwt = source,
            authorities = authorities,
            portfolio = portfolio!!
        )
    }
}