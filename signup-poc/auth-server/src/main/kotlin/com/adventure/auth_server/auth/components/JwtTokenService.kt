@file:Suppress("DEPRECATION")

package com.adventure.auth_server.auth.components

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Suppress("DEPRECATION")
@Service
class JwtTokenService {

    @Value("\${jwt.secret-key}")
    private lateinit var secretKeyString: String

    private val hashedSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }


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
            .signWith(HS256, hashedSecretKey)
            .compact()
    }
}
