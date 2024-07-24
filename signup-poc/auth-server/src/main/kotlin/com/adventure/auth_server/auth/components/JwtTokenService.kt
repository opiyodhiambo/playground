@file:Suppress("DEPRECATION")

package com.adventure.auth_server.auth.components

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.*
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Suppress("DEPRECATION")
@Service
class JwtTokenService {

    private val secretKey = "auth-server-signing-key"

    fun generateToken(username: String): String {
        val now = Date()
        val validity = Date(now.time + 3600000)

        val claims = Jwts
            .claims()
            .subject(username)
            .build()

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(validity)
            .signWith(HS256, secretKey)
            .compact()
    }
}