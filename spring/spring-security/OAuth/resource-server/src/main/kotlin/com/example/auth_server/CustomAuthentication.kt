package com.example.auth_server

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class CustomAuthentication(
    jwt: Jwt,
    authorities: Collection<GrantedAuthority>,
    val priority: String
): JwtAuthenticationToken(jwt, authorities)