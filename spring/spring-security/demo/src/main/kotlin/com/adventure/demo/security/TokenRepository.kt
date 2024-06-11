package com.adventure.demo.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface TokenRepository: JpaRepository<Token, UUID> {
    fun findTokenByIdentifier(identifier: String): Optional<Token>
}