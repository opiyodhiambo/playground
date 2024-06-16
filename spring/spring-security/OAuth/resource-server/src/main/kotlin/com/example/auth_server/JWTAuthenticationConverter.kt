package com.example.auth_server

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JWTAuthenticationConverter: Converter<Jwt, CustomAuthentication> {
    override fun convert(source: Jwt): CustomAuthentication {
        val authorities: List<GrantedAuthority> = listOf(GrantedAuthority { "read" })
        val priority = source.claims["priority"].toString()
        return CustomAuthentication(source, authorities, priority)
    }
}